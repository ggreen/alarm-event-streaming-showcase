package showcase.streaming.event.rabbitmq.streaming;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.mqttv5.client.IMqttClient;
import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MqttConfig
 *
 * @author Gregory Green
 */
@Configuration
@Slf4j
public class MqttConfig
{
    @Value("${spring.application.name:http-mqtt-source}")
    private String clientId;

    @Value("${mqtt.connectionUrl:tcp://localhost:1883}")
    private String connectionUrl;

    @Value("${mqtt.userName:guest}")
    private String userName;

    @Value("${mqtt.userPassword:guest}")
    private String userPassword;

    @Value("${mqtt.timeout:500}")
    private int timeout;


    @Bean
    IMqttClient mqttClient() throws MqttException
    {
        var mqttClient = new MqttClient(connectionUrl, clientId, new MemoryPersistence());

        // 2. Set Connection Options
        MqttConnectionOptions options = new MqttConnectionOptions();
        options.setCleanStart(true); // Setting clean start to true (clean session in MQTT v3)
        options.setConnectionTimeout(timeout);
        options.setAutomaticReconnect(true);

        // 3. Connect to the Broker3
        log.info("Attempting to connect to broker: {}", connectionUrl);
        mqttClient.connect(options);

        return mqttClient;
    }

}
