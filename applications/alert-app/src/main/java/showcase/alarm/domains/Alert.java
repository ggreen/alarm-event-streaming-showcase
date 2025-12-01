package showcase.alarm.domains;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

public record Alert(@Id @NotNull String id, @NotNull String level,
                    String time,
                    LocalDateTime dateTime, @NotNull String event) {
}
