package showcase.streaming.event.rabbitmq.streaming.controller;

import org.eclipse.paho.mqttv5.client.IMqttClient;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MqttPublisherControllerTest {


    private MqttPublisherController subject;
    private String topic = "junit";
    private String body = "body junit";

    @Mock
    private IMqttClient client;

    @BeforeEach
    void setUp() {
        subject = new MqttPublisherController(client);
    }

    @Test
    void send() throws MqttException {
        Map<String, String> headers = Map.of("user","agent");
        assertDoesNotThrow(()->subject.post(headers,topic,body));
    }
}