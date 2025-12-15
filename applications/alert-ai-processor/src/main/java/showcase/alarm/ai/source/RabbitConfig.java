package showcase.alarm.ai.source;

import com.rabbitmq.stream.MessageHandler;
import com.rabbitmq.stream.OffsetSpecification;
import nyla.solutions.core.util.Text;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.cloud.stream.config.ListenerContainerCustomizer;
import org.springframework.cloud.stream.config.ProducerMessageHandlerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.outbound.RabbitStreamMessageHandler;
import org.springframework.rabbit.stream.listener.StreamListenerContainer;
import org.springframework.rabbit.stream.producer.RabbitStreamTemplate;

@Configuration
public class RabbitConfig {

    @Bean
    ListenerContainerCustomizer<MessageListenerContainer> customizer() {
        return (cont, dest, group) -> {
            StreamListenerContainer container = (StreamListenerContainer) cont;
            container.setConsumerCustomizer((name, builder) -> {
                builder.offset(OffsetSpecification.first())
                        .name(Text.generator().generateId()); //TODO: fix in future
            });
        };
    }

//    @Bean
//    ProducerMessageHandlerCustomizer<MessageHandler> handlerCustomizer() {
//        return (hand, dest) -> {
//            RabbitStreamMessageHandler handler = (RabbitStreamMessageHandler) hand;
//            handler.setConfirmTimeout(5000);
//            ((RabbitStreamTemplate) handler.getStreamOperations()).setProducerCustomizer(
//                    (name, builder) -> {
//                    builder.
//                    });
//        };
//    }

}
