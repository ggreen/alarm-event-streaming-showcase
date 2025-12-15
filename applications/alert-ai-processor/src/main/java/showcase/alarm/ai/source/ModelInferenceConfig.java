package showcase.alarm.ai.source;

import io.netty.channel.ChannelOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.boot.web.client.RestClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import showcase.alarm.ai.source.service.ai.AlertsModelInference;
import showcase.streaming.domains.AlertList;

import java.time.Duration;

@Configuration
@Slf4j
public class ModelInferenceConfig {

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
    public RestClientCustomizer customRestClientTimeout() {
        return restClientBuilder -> {
            SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();

            // Set Connect and Read timeouts
            factory.setConnectTimeout(Duration.ofSeconds(10));
            factory.setReadTimeout(Duration.ofSeconds(60));

            restClientBuilder.requestFactory(factory);
        };
    }

    @Bean
    public WebClient.Builder webClientBuilder() {

        HttpClient httpClient = HttpClient.create()
                // 1. Connection Timeout (time to establish connection)
                // Netty ChannelOption is in milliseconds
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000) // 10 seconds

                // 2. Response Timeout (time to receive the full response)
                .responseTimeout(Duration.ofSeconds(60)) // 60 seconds

                // 3. Optional: Fine-grained Read/Write timeouts (time between data chunks)
                .doOnConnected(conn ->
                        conn.addHandlerLast(
                                new io.netty.handler.timeout.ReadTimeoutHandler(30) // 30 seconds inactivity
                        )
                );

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient));
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
