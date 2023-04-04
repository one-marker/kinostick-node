package kinostick.stream.exeption;

import java.util.EmptyStackException;

public class InvalidTorrentException extends EmptyStackException {

    private String msg = "";

    public InvalidTorrentException() {

    }
    public InvalidTorrentException(String msg) {
        this.msg = msg;
    }
    public String getMsg() {
        return msg;
    }
}
