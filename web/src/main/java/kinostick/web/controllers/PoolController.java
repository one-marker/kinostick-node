package kinostick.web.controllers;

import kinostick.stream.service.StreamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;

@Slf4j
@RestController
@RequestScope
public class PoolController {

    @Autowired
    private StreamService streamService;

    @GetMapping("/pool/free")
    public Integer countFreeChannels() {
        return streamService.getPoolSize()-streamService.getProcesses().size();
    }
    @GetMapping("/pool/size")
    public Integer getPoolSize() {
        return streamService.getPoolSize();
    }

}
