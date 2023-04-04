package kinostick.api.model.stream.open;

import lombok.*;
import lombok.extern.jackson.Jacksonized;


//@NoArgsConstructor
//@AllArgsConstructor
public class OpenStreamRq {

    private String magnet;

    private String device;

    public OpenStreamRq() {
    }
    public OpenStreamRq(String magnet, String device) {
        this.magnet = magnet;
        this.device = device;
    }

    public String getMagnet() {
        return magnet;
    }

    public void setMagnet(String magnet) {
        this.magnet = magnet;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }
}
