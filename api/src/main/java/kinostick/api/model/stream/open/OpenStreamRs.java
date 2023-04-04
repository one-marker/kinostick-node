package kinostick.api.model.stream.open;


import lombok.*;

@Data
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpenStreamRs {

    private String hostname;

    private String uuid;

}
