package showcase.streaming.event.rabbitmq.streaming.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.mqttv5.client.IMqttClient;
import org.eclipse.paho.mqttv5.client.IMqttToken;
import org.eclipse.paho.mqttv5.client.MqttCallback;
import org.eclipse.paho.mqttv5.client.MqttDisconnectResponse;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;
import org.eclipse.paho.mqttv5.common.packet.UserProperty;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MqttPublisherController {

    private final IMqttClient client;
    private MqttCallback callback = new MqttCallback() {
        @Override
        public void disconnected(MqttDisconnectResponse mqttDisconnectResponse) {

        }

        @Override
        public void mqttErrorOccurred(MqttException e) {
            log.info("Exception: {}",e);

        }

        @Override
        public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {

        }

        @Override
        public void deliveryComplete(IMqttToken iMqttToken) {
            log.info("token: {}",iMqttToken);
        }

        @Override
        public void connectComplete(boolean b, String s) {

        }

        @Override
        public void authPacketArrived(int i, MqttProperties mqttProperties) {

        }
    };

    @PostMapping("mqtt")
    @ResponseStatus(HttpStatus.OK)
    void post(@RequestHeader Map<String, String> httpHeaders, @RequestParam String topic, @RequestBody String body) throws MqttException {
        var msg = new MqttMessage(body.getBytes(StandardCharsets.UTF_8));
        var properties = new MqttProperties();
        var userProperties = new ArrayList<UserProperty>();

        client.setCallback(callback);


        if(httpHeaders != null)
        {
            for (var entry : httpHeaders.entrySet())
            {
                var value = entry.getValue();
                userProperties.add(new UserProperty(entry.getKey(),value));
            }
        }

        properties.setUserProperties(userProperties);
        msg.setProperties(properties);
        msg.setQos(1);
        client.publish(topic,msg);

        log.info(" Published to topic: {} body: {}",topic,body);
    }

}
