package showcase.alarm.ai.source;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.stream.OffsetSpecification;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import nyla.solutions.core.util.Text;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.cloud.function.context.config.JsonMessageConverter;
import org.springframework.cloud.function.json.JacksonMapper;
import org.springframework.cloud.stream.config.ListenerContainerCustomizer;
import org.springframework.cloud.stream.config.ProducerMessageHandlerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.outbound.RabbitStreamMessageHandler;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.rabbit.stream.listener.StreamListenerContainer;
import org.springframework.rabbit.stream.producer.RabbitStreamTemplate;

@Configuration
@Slf4j
public class RabbitConfig {

    private String streamName;

    @Bean
    ListenerContainerCustomizer<MessageListenerContainer> customizer() {
        return (cont, dest, group) -> {
            if(cont instanceof StreamListenerContainer container)
            {
                container.setConsumerCustomizer((name, builder) -> {
                    builder.offset(OffsetSpecification.first())
                            .name(Text.generator().generateId()); //TODO: fix in future

                });
            }
        };
    }

    @Bean
    org.springframework.messaging.converter.MessageConverter messageConverter(ObjectMapper objectMapper)
    {
      var jsonConvert =  new JsonMessageConverter(new JacksonMapper(objectMapper));

      return new MessageConverter() {
          @SneakyThrows
          @Override
          public Object fromMessage(Message<?> message, Class<?> targetClass) {
              log.info("fromMessage.payload: {}",message.getPayload());
              var results = jsonConvert.fromMessage(message,targetClass);
              log.info("results={}",results);
              if(results !=null)
                return results;

              if( message.getPayload() instanceof byte[] payloadBytes)
              {
                      var messageString = new String(payloadBytes);
                      log.info("messageString: {}, targetClass:{}",messageString,targetClass.getName());
                      return objectMapper.readValue(messageString,targetClass);
              }

              return null;
          }

          @Override
          public Message<?> toMessage(Object payload, MessageHeaders headers) {
              log.info("toMessage: {}",payload);
              return jsonConvert.toMessage(payload,headers);
          }
      };
    }

    @Bean
    ProducerMessageHandlerCustomizer<MessageHandler> handlerCustomizer() {
        return (hand, dest) -> {
            RabbitStreamMessageHandler handler = (RabbitStreamMessageHandler) hand;
            handler.setConfirmTimeout(5000);
            var rabbitStreamTemplate = ((RabbitStreamTemplate) handler.getStreamOperations());
//            rabbitStreamTemplate.setMessageConverter(messageConverter);
        };
    }

}
