package kinostick.stream.model;

import kinostick.stream.service.ExecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.stream.IntStream;

@Component
public class Pool {

    private Stack<Integer> freePorts = new Stack<>();
    private Set<Integer> reservedPorts = new HashSet<>();


    private Integer beginPort;
    private Integer endPort;

    @Autowired
    ExecService execService;

    public Pool(@Value("${streams.ports.begin}") Integer beginPort,
                @Value("${streams.ports.end}") Integer endPort) {
        this.beginPort = beginPort;
        this.endPort = endPort;

        IntStream.rangeClosed(beginPort, endPort).forEach(port -> freePorts.push(port));
    }

    public Integer getPoolSize() {
        return endPort - beginPort;
    }

    public Integer reservePort() {
        if (freePorts.isEmpty()) {
            return -1;
        }
        Integer port = freePorts.pop();
//        reservedPorts.add(port);upnpRule(UPnp.ADD, String.valueOf(port));
        return port;
    }

    public Integer freePort(Integer port) {
        reservedPorts.remove(port);
        freePorts.push(port);
//        upnpRule(UPnp.DELETE, String.valueOf(port));
        return port;
    }

    /*

    //todo открыть порт на хосте по протоколу upnp
    private void upnpRule(UPnp rule, String port) {
        String cmd = String.format("java -jar /Users/marker/Downloads/portmapper-2.2.1.jar -lib org.chris.portmapper.router.weupnp.WeUPnPRouterFactory  -%s -externalPort %s -internalPort %s -protocol tcp -description KINOSTICK", rule.getOperation(), port, port);
        try {
            execService.execute(cmd);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

     */
}
