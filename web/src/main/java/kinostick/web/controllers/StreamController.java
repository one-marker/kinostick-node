package kinostick.web.controllers;


import kinostick.api.controllers.ApiController;
import kinostick.api.model.device.DeviceEnum;
import kinostick.api.model.stream.close.CloseStreamRq;
import kinostick.api.model.stream.open.OpenStreamRq;
import kinostick.stream.exeption.InvalidTorrentException;
import kinostick.stream.exeption.NoFreePortsException;
import kinostick.stream.model.Memory;
import kinostick.stream.model.Movie;
import kinostick.stream.service.ExecService;
import kinostick.stream.service.NetworkService;
import kinostick.stream.service.StreamService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestScope
public class StreamController extends ApiController {



    @Autowired
    private ExecService execService;
    @Autowired
    private Memory memory;
    @Autowired
    private StreamService streamService;


    @GetMapping("/processes")
    public String getProcesses() {
        Map<String, Movie> map = streamService.getProcesses();
        return map.keySet().stream()
                .map(key -> key + "=" + map.get(key).toString())
                .collect(Collectors.joining(", ", "{", "}"));
//        return streamService.getProcesses();
    }


    @GetMapping("/pool/free")
    public Integer countFreeChannels() {
        return streamService.getPoolSize()-streamService.getProcesses().size();
    }
    @GetMapping("/pool/size")
    public Integer getPoolSize() {
        return streamService.getPoolSize();
    }

    @RequestMapping(value = "/{device}/{id}", method = RequestMethod.GET)
    public void redirectIOS(@PathVariable("device") String device, @PathVariable("id") String id, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

        log.info(device);
        log.info(id);
        log.info(httpServletRequest.getScheme() + "://"+ httpServletRequest.getLocalAddr());

        if (memory.getUrlById(id) == null) {
            httpServletResponse.setStatus(404);
            return;
        }
        switch (device) {
            case "android":
                httpServletResponse.setHeader("Location", "vlc://"+memory.getUrlById(id));
                break;
            case "ios":
                httpServletResponse.setHeader("Location", "vlc-x-callback://x-callback-url/ACTION?url="+memory.getUrlById(id));
        }
        httpServletResponse.setStatus(302);
    }

//    @RequestMapping(value = "/android/{id}", method = RequestMethod.GET)
//    public void redirectANDROID(@PathVariable("id") String id, HttpServletResponse httpServletResponse) {
//
//        if (memory.getUrlById(id) == null) {
//            httpServletResponse.setStatus(404);
//            return;
//        }
//
//        httpServletResponse.setHeader("Location", "vlc://"+memory.getUrlById(id));
//        httpServletResponse.setStatus(302);
//    }

    @PostMapping("/close")
    public String close(@RequestBody CloseStreamRq rq) {
        return streamService.closeConnection(rq.getUuid());
    }

    @PostMapping("/stream")
    public String stream(@RequestBody OpenStreamRq rq) {
        DeviceEnum deviceEnum = DeviceEnum.valueOf(rq.getDevice());

        try{
            String uuid = streamService.openConnection(rq.getMagnet());

            switch (deviceEnum) {
                case IOS:
                    return uuid;
                case STREAM:
                    return memory.getUrlById(uuid) + "\nuuid:" + uuid;
            }
            return uuid;
        } catch (NoFreePortsException e) {
            return e.getMsg();
        } catch (InvalidTorrentException e) {
            return e.getMsg();
        }
    }

}
