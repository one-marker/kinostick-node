package kinostick.stream.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class ExecService {

    public String execute(String command) throws IOException, InterruptedException {

        StringBuilder log = new StringBuilder();
        Process proc = Runtime.getRuntime().exec(command);

        // Read the output

        BufferedReader reader =
                new BufferedReader(new InputStreamReader(proc.getInputStream()));

        String line = "";
        while((line = reader.readLine()) != null) {
            System.out.print(line + "\n");
            log.append(line + "\n");
        }

        proc.waitFor();

        return String.valueOf(log);
    }
}
