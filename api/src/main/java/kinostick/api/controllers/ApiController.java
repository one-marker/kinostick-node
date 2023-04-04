package kinostick.api.controllers;


import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

@Component
public class ApiController {

    @GetMapping("/ping")
    String ping() {
        return "pong";
    }


}
