package kinostick.web.controllers;

import kinostick.stream.service.ProxyManagerService;
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
    private ProxyManagerService proxyManagerService;
    @Autowired
    private NetworkService networkService;

    @GetMapping("/reload")
    public String reload() {
        networkService.nginxReload();
        return "OK";
    }

    @GetMapping("/proxy/{name}/{port}/{path}")
    public String proxy(@PathVariable String name, @PathVariable String port, @PathVariable String path) {
        proxyManagerService.createProxy(name, Integer.valueOf(port), path.replace('_','/'));
        networkService.nginxReload();
        return "OK";
    }

    @GetMapping("/close/{name}")
    public String close(@PathVariable String name) {
        proxyManagerService.removeProxy(name);
        networkService.nginxReload();
        return "OK";
    }
}
