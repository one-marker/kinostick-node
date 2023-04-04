package kinostick.api.model.stream.close;


import lombok.*;

@Data
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CloseStreamRq {

    private String uuid;
}
