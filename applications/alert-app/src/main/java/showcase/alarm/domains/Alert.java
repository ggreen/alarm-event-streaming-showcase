package showcase.alarm.domains;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;

public record Alert(@Id @NotNull String id,
                    @NotNull String account,
                    @NotNull String level,
                    String time,
                    @NotNull String event) {
}
