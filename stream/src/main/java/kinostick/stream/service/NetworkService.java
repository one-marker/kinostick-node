package kinostick.stream.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Service
public class NetworkService {

    @Value("${server.hostname}")
    private String hostname;
    @Value("${server.port}")
    private Integer port;


    private final Set<String> whiteList = new HashSet<>();
    @Autowired
    private ExecService execService;

    public String getHostname() {
        return hostname;
    }

    public String accessClient(String ip, Integer port) {
        if (whiteList.contains(ip)) {
            return "ip already in the list";
        }
        try {

            execService.execute("iptables -I INPUT -p tcp -s " + ip + " --dport " + port + " -j ACCEPT");
            whiteList.add(ip);
            return String.format("access to %d granted for %s:", port, ip);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String restart() {

        try {
            execService.execute("service nginx reload");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return "OK";
    }
}
