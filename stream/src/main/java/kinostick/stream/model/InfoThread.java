package kinostick.stream.model;

import kinostick.stream.model.format.VideoType;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.EnumUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class InfoThread implements Callable<List<TorrentFile>> {

    String magnet;

    public InfoThread(String magnet) {
        this.magnet = magnet;
    }

    @Override
    public List<TorrentFile> call() throws Exception {

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
}
