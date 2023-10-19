package kinostick.stream.service;

import kinostick.stream.exeption.InvalidTorrentException;
import kinostick.stream.exeption.NoFreePortsException;
import kinostick.stream.model.Memory;
import kinostick.stream.model.Movie;
import kinostick.stream.model.Pool;
import kinostick.stream.model.TorrentFile;
import kinostick.stream.model.format.VideoType;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Service
public class StreamService {

    @Value("${tmp.dir}")
    private String tmpDir;
    @Autowired
    private ProxyManagerService proxyManagerService;
    @Autowired
    private Pool pool;
    @Autowired
    private Memory memory;
    @Autowired
    private NetworkService networkService;

    public String openConnection(String index, String magnet) throws NoFreePortsException {

        String movie = memory.getMagnetMovieMap(magnet);

        if (movie != null) {
            return movie;
        }

        return index.isEmpty()
                ? memory.putMovie(startStreaming(UUID.randomUUID().toString(), magnet))
                : memory.putMovie(startStreaming(index, UUID.randomUUID().toString(), magnet));


    }

    public Integer getPoolSize() {
        return pool.getPoolSize();
    }

    public Map<String, Movie> getProcesses() {
        return memory.getIdMovieMap();
    }

    public List<TorrentFile> getFileList(String magnet) {

        List<TorrentFile> torrentFileList = new ArrayList<>();
        String[] cmd = {"kinostix", magnet, "--list"};

        try {
            Process process = new ProcessBuilder(cmd)
                    .start();


            // Start a new java process


            // Read and print the standard output stream of the process
            try (BufferedReader input =
                         new BufferedReader(new
                                 InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = input.readLine()) != null) {

                    System.out.println(line);

                    if (line.contains(".")) {

                        line = line.replace("\u001B", "");
                        line = line.replaceAll("[\\[0-9]+?[Bmh]", "");
                        line = line.replace(" ", "");

                        String[] split = line.split(":");

                        String filename = new File(split[1]).getName();
                        String extension = FilenameUtils.getExtension(filename).toUpperCase();

                        if (!EnumUtils.isValidEnum(VideoType.class, extension)) {
                            continue;
                        }

                        System.out.println(split[0]);
                        torrentFileList.add(new TorrentFile(split[0],
                                FilenameUtils.removeExtension(new File(split[1]).getName()),
                                VideoType.valueOf(extension),
                                split[2].strip()));
                    }
                }
                int status = process.waitFor();
                System.out.println("Exited with status: " + status);
                process.destroy();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return torrentFileList;
    }

    private Movie startStreaming(String uuid, String magnet) {
        int port = pool.reservePort();

        if (port == -1) {
            throw new NoFreePortsException();
        }

        String[] cmd = {"kinostix", "-p", String.valueOf(port), magnet, "--url", contextPathHLS(uuid), "--path", allocateDir(uuid), "--remove"};

        Process process;
        try {
            process = new ProcessBuilder(cmd)
                    .redirectError(ProcessBuilder.Redirect.PIPE)
                    .redirectOutput(ProcessBuilder.Redirect.PIPE)
                    .start();

            Thread.sleep(500);

            if (process.getErrorStream().available() == 27) {
                System.out.println("Invalid torrent identifier");
                throw new InvalidTorrentException("InvalidTorrentException");
            }

        } catch (InvalidTorrentException e) {
            throw e;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        proxyManagerService.createProxy(uuid, port, contextPathHLS(uuid));
        networkService.nginxReload();

        return new Movie(uuid, networkService.getHostname(), port, getFullPathHLS(contextPathHLS(uuid)), magnet, process);
    }

    private Movie startStreaming(String index, String uuid, String magnet) {
        int port = pool.reservePort();

        if (port == -1) {
            throw new NoFreePortsException();
        }

        String[] cmd = {"kinostix", "-p", String.valueOf(port), magnet, "--url", contextPathHLS(uuid), "--index", index, "--path", allocateDir(uuid), "--remove"};

        Process process;
        try {
            process = new ProcessBuilder(cmd)
                    .redirectError(ProcessBuilder.Redirect.PIPE)
                    .redirectOutput(ProcessBuilder.Redirect.PIPE)
                    .start();

            Thread.sleep(500);

            if (process.getErrorStream().available() == 27) {
                System.out.println("Invalid torrent identifier");
                throw new InvalidTorrentException("InvalidTorrentException");
            }

        } catch (InvalidTorrentException e) {
            throw e;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        proxyManagerService.createProxy(uuid, port, contextPathHLS(uuid));
        networkService.nginxReload();

        return new Movie(uuid, networkService.getHostname(), port, getFullPathHLS(contextPathHLS(uuid)), magnet, process);
    }

    public String closeConnection(String uuid) {

        Movie movie = memory.getMovieById(uuid);
        //todo получить порт из URL
        if (movie != null) {
            int port = movie.getPort();
            pool.freePort(port);
            memory.removeMovieById(uuid);
            removeTmp(uuid);
            proxyManagerService.removeProxy(uuid);
            networkService.nginxReload();
        }

        return "Соединение закрыто";

    }

    public void removeTmp(String uuid) {
        File directoryToDelete = new File(allocateDir(uuid));
        FileSystemUtils.deleteRecursively(directoryToDelete);
    }

    private String allocateDir(String uuid) {
        return tmpDir + uuid.trim().strip();
    }

    private String getFullPathHLS(String url) {
        return String.format("https://%s%s", networkService.getHostname(), url);
    }

    private String contextPathHLS(String uuid) {
        return String.format("/hls/%s/", uuid);
    }
}
