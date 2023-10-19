package kinostick.stream.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import kinostick.stream.model.format.VideoType;


public class TorrentFile {

    @JsonProperty
    String id;

    @JsonProperty
    String name;

    @JsonProperty
    VideoType type;

    @JsonProperty
    String size;

    public TorrentFile(String id, String name, VideoType type, String size) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.size = size;
    }
}
