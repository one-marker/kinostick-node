package kinostick.stream.service;

import kinostick.stream.exeption.InvalidTorrentException;
import kinostick.stream.exeption.NoFreePortsException;
import kinostick.stream.model.Memory;
import kinostick.stream.model.Movie;
import kinostick.stream.model.Pool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;


@Service
public class StreamService {

    @Value("${tmp.dir}")
    private String tmpDir;

    @Autowired
    private Pool pool;
    @Autowired
    private Memory memory;
    @Autowired
    private NetworkService networkService;

    public String openConnection(String magnet) throws NoFreePortsException {

        String movie = memory.getMagnetMovieMap(magnet);

        if (movie != null) {
            return movie;
        }

        return memory.putMovie(startPeerFlix(UUID.randomUUID().toString(), magnet));
    }

    public Integer getPoolSize() {
        return pool.getPoolSize();
    }

    public Map<String, Movie> getProcesses() {
        return memory.getIdMovieMap();
//        Map<String, Movie> map = memory.getIdMovieMap();
//        return map.keySet().stream()
//                .map(key -> key + "=" + map.get(key).toString())
//                .collect(Collectors.joining(", ", "{", "}"));
    }

    private Movie startPeerFlix(String uuid, String magnet) {
//        String ip = networkService.getIp();
        int port = pool.reservePort();

        if (port == -1) {
            throw new NoFreePortsException();
        }

        String[] cmd = {"kinostix", "-p", String.valueOf(port), magnet, "--path", allocateDir(uuid), "--remove"};

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

        return new Movie(uuid, networkService.getHostname(), port, generateMovieLink(networkService.getHostname(), port), magnet, process);
    }

    public String closeConnection(String uuid) {

        Movie movie = memory.getMovieById(uuid);
        //todo получить порт из URL
        if (movie != null) {
            int port = movie.getPort();
            pool.freePort(port);
            memory.removeMovieById(uuid);
            removeTmp(uuid);
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

    private String generateMovieLink(String host, int port) {
        return String.format("http://%s:%d/", host, port);
    }

}
