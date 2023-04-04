package kinostick.stream.exeption;

import java.util.EmptyStackException;

public class NoFreePortsException extends EmptyStackException {

    private String msg = "Все каналы зарезервированы";

    public NoFreePortsException() {

    }

    public NoFreePortsException(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
