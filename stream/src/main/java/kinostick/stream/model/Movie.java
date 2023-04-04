package kinostick.stream.model;

public class Movie {

    private String uuid;
    private String url;
    private String magnet;
    private Process process;
    private String ip;
    private Integer port;
    private String folderName;

    public Movie(String uuid, String ip, Integer port, String url, String magnet, Process process) {
        this.uuid = uuid;
        this.url = url;
        this.magnet = magnet;
        this.process = process;
        this.ip = ip;
        this.port = port;
    }

    public Movie(String uuid, String url, String magnet, Process process) {
        this.uuid = uuid;
        this.url = url;
        this.magnet = magnet;
        this.process = process;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMagnet() {
        return magnet;
    }

    public void setMagnet(String magnet) {
        this.magnet = magnet;
    }

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }


    @Override
    public String toString() {
        return "Movie{" +
                "uuid='" + uuid + '\'' +
                ", url='" + url + '\'' +
                ", magnet='" + magnet + '\'' +
                ", process=" + process +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", folderName='" + folderName + '\'' +
                '}';
    }
}
