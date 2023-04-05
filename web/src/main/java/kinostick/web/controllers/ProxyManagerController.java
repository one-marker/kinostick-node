package kinostick.web.controllers;

import kinostick.stream.model.Proxy;
import kinostick.stream.service.NetworkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;

@Slf4j
@RestController
@RequestScope
public class ProxyManagerController {


    @Autowired
    private Proxy proxy;
    @Autowired
    private NetworkService networkService;

    @GetMapping("/reload")
    public String reload() {
        networkService.restart();
        return "OK";
    }

    @GetMapping("/proxy/{name}/{port}/{path}")
    public String proxy(@PathVariable String name, @PathVariable String port, @PathVariable String path) {
        proxy.createProxy(name, Integer.valueOf(port), path.replace('_','/'));
        networkService.restart();
        return "OK";
    }

    @GetMapping("/close/{name}")
    public String close(@PathVariable String name) {
        proxy.removeProxy(name);
        networkService.restart();
        return "OK";
    }
}
