package showcase.alarm.ai.source;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.boot.web.client.RestClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import showcase.alarm.ai.source.service.AlertDetectorService;
import showcase.alarm.ai.source.service.ai.AlertsModelInference;
import showcase.streaming.domains.Alert;
import showcase.streaming.domains.AlertList;

import java.time.Duration;
import java.util.List;

@Configuration
@Slf4j
public class AiModelInferenceConfig {

    private static final String prompt = """
            Given the following activities, identify alerts,
            For each alert response with the "level" with values of (CRITICAL, HIGH, MEDIUM, Low),
            the time and the event which contains why you believe this is an alert
            
            ONLY RESPONSE WITH Json Object fields level, account, time, and event
            
            ```json
            {listOfActivities}
            ```
            
            Use Context Below
            CONTEXT:
            DO NOT alert when Opened door or Garage Door AND the door is closed at a later time
            DO NOT alert when Alarm System Turned On
            DO NOT alert when Alarm System Turned OFF Successfully
            Door Opened with No Door Closed is a MEDIUM Alert
            """;
    // Define a long timeout, e.g., 3 minutes (180 seconds)
    private static final Duration OLLAMA_READ_TIMEOUT = Duration.ofMinutes(5);

    @Bean
    public RestClientCustomizer ollamaReadTimeoutCustomizer() {
        return restClientBuilder -> {
            SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
            // This is the key setter for the read timeout
            factory.setReadTimeout(OLLAMA_READ_TIMEOUT);

            // Optionally, set a connection timeout as well
            // factory.setConnectTimeout(Duration.ofSeconds(30));

            restClientBuilder.requestFactory(factory);
        };
    }
    @Bean
    ChatClient chatClient(ChatModel chatModel){
        return ChatClient.create(chatModel);
    }

    @Bean
    AlertsModelInference alertsModelInference(ChatClient chatClient){

        return listOfActivities -> {

            synchronized (this) {

                var alertList = chatClient.prompt()
                        .user(u -> u.text(prompt)
                                .param("listOfActivities", listOfActivities))
                        .call()
                        .entity(AlertList.class);
                log.info("alerts: {}", alertList);

                return alertList != null ? alertList.alerts() : null;
            }
        };
    }
}
