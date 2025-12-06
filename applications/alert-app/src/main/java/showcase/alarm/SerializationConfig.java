package showcase.alarm;

import nyla.solutions.core.patterns.conversion.Converter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import showcase.alarm.domains.Alert;
import tools.jackson.databind.ObjectMapper;

@Configuration
public class SerializationConfig {
    @Bean
    Converter<byte[], Alert> converter(ObjectMapper objectMapper)
    {
        return msg -> objectMapper.readValue(new String(msg), Alert.class);
    }
}
