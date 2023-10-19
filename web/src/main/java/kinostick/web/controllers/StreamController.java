package kinostick.web.controllers;

import kinostick.api.controllers.ApiController;
import kinostick.api.model.device.DeviceEnum;
import kinostick.api.model.stream.close.CloseStreamRq;
import kinostick.api.model.stream.open.OpenStreamRq;
import kinostick.stream.exeption.InvalidTorrentException;
import kinostick.stream.exeption.NoFreePortsException;
import kinostick.stream.model.Memory;
import kinostick.stream.model.Movie;
import kinostick.stream.service.StreamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    private Memory memory;
    @Autowired
    private StreamService streamService;

    @PostMapping("/info")
    public ResponseEntity<?> info( @RequestBody OpenStreamRq rq) {


        return new ResponseEntity<>(streamService.getFileList(rq.getMagnet()), HttpStatus.OK);
//"magnet:?xt=urn:btih:d10a4157348ded912d08ad2c0a054f7c5c12b5d6&dn=Interstelar.2014.IMAX.RUS.BDRip.x264.-HELLYWOOD.mkv&tr=udp%3A%2F%2Fshubt.net%3A2710&tr=udp%3A%2F%2Ftracker.openbittorrent.com%3A80%2Fannounce&tr=udp%3A%2F%2Ftracker.publicbt.com%3A80%2Fannounce&tr=udp%3A%2F%2Ftracker.ccc.de%3A80%2Fannounce&tr=http%3A%2F%2Fretracker.local%2Fannounce&tr=udp%3A%2F%2Fopen.demonii.com%3A1337&tr=http%3A%2F%2Ftracker.filetracker.pl%3A8089%2Fannounce&tr=http%3A%2F%2Ftracker2.wasabii.com.tw%3A6969%2Fannounce&tr=http%3A%2F%2Ftracker.grepler.com%3A6969%2Fannounce&tr=http%3A%2F%2F125.227.35.196%3A6969%2Fannounce&tr=http%3A%2F%2Ftracker.tiny-vps.com%3A6969%2Fannounce&tr=http%3A%2F%2F87.248.186.252%3A8080%2Fannounce&tr=http%3A%2F%2F210.244.71.25%3A6969%2Fannounce&tr=http%3A%2F%2F46.4.109.148%3A6969%2Fannounce&tr=http%3A%2F%2Ftracker.dler.org%3A6969%2Fannounce&tr=udp%3A%2F%2F%5B2001%3A67c%3A28f8%3A92%3A%3A1111%3A1%5D%3A2710&tr=udp%3A%2F%2Fipv6.leechers-paradise.org%3A6969&tr=udp%3A%2F%2F46.148.18.250%3A2710"

    }

    @GetMapping("/processes")
    public String getProcesses() {
        Map<String, Movie> map = streamService.getProcesses();
        return map.keySet().stream()
                .map(key -> key + "=" + map.get(key).toString())
                .collect(Collectors.joining(", ", "{", "}"));
    }

    @RequestMapping(value = "/{device}/{id}", method = RequestMethod.GET)
    public void redirect(@PathVariable("device") String device, @PathVariable("id") String id, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

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


    @PostMapping("/stream")
    public String stream(@RequestParam(required = false, defaultValue = "") String index, @RequestBody OpenStreamRq rq) {
        DeviceEnum deviceEnum = DeviceEnum.valueOf(rq.getDevice());

        try{
            String uuid = streamService.openConnection(index, rq.getMagnet());

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


    @PostMapping("/close")
    public String close(@RequestBody CloseStreamRq rq) {
        return streamService.closeConnection(rq.getUuid());
    }


}
