package showcase.alarm;

import com.rabbitmq.client.amqp.*;
import com.rabbitmq.client.amqp.impl.AmqpEnvironmentBuilder;
import lombok.extern.slf4j.Slf4j;
import nyla.solutions.core.patterns.conversion.Converter;
import nyla.solutions.core.util.Debugger;
import org.bouncycastle.pqc.crypto.ExchangePair;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import showcase.alarm.domains.Alert;
import tools.jackson.databind.ObjectMapper;

@Configuration
@Slf4j
class RabbitAmqpConfig {

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${spring.rabbitmq.host:localhost}")
    private String host;

    @Value("${spring.rabbitmq.username:guest}")
    private String username;

    @Value("${spring.rabbitmq.password:guest}")
    private String password;


    @Value("${stream.filter.offset:FIRST}")
    private String offsetName;

    //@Value("${spring.cloud.stream.bindings.input.destination:inputFilter}.${spring.cloud.stream.bindings.input.group}")
    @Value("${stream.destination:alerts.alert}")
    private String streamName;

    @Value("${stream.filter.sql}")
    private String sqlFilter;

    @Value("${stream.filter.value:}")
    private String filterValue;


    @Value("${spring.cloud.stream.bindings.input.destination:amq.topic}")
    private String exchange;


    @Value("${stream.exchange.bind.key:#}")
    private String bindRoutingKey;

    @Bean
    Environment amqpEnvironment()
    {
        return new AmqpEnvironmentBuilder()
                .build();
    }

    @Bean("inputConnection")
    Connection amqpConnection(Environment environment)
    {
        return environment.connectionBuilder().host(host)
                .name(applicationName)
                .username(username)
                .password(password)
                .build();
    }



    @Bean
    Consumer consumer(@Qualifier("inputConnection") Connection connection,
                      @Qualifier("input") Management.QueueInfo input,
                      java.util.function.Consumer<Alert> alertConsumer,
                      Converter<byte[], Alert> messageConverter){

        log.info("input consumed with SQL '{}' from input stream {}",sqlFilter,input.name());

        var builder = connection.consumerBuilder()
                .queue(input.name())
                .stream()
                .offset(ConsumerBuilder.StreamOffsetSpecification.valueOf(offsetName));

        if(!filterValue.isEmpty())
        {
            log.info("Adding filter value: {}",filterValue);
            builder = builder.filterValues(filterValue);
        }

        /*
        builder
                .filter()
                .sql(sqlFilter)
                .stream()
         */

        /*
        application Properties
        content-type=application/json,
        accountid=imani, level=critical, content-length=111} }
         */
        return builder
                .filter()
                .sql(sqlFilter)
                .stream()
                .builder().messageHandler((ctx,inputMessage) -> {

                    try {
                        //Processing input message
                        log.info("Processing input: {}, msg id: {}", streamName, inputMessage.messageId());
                        alertConsumer.accept(messageConverter.convert(inputMessage.body()));
                    }
                    catch (Exception e)
                    {
                        log.error(Debugger.stackTrace(e));
                        throw e;
                    }

                })
                .build();
    }

    @Bean
    Converter<byte[], Alert> converter(ObjectMapper objectMapper)
    {
        return msg -> objectMapper.readValue(new String(msg), Alert.class);
    }


    @Bean("inputManagement")
    Management amqpManagement(@Qualifier("inputConnection") Connection connection)
    {
        return connection.management();
    }

    @Bean("input")
    Management.QueueInfo inputQueue(@Qualifier("inputManagement") Management management)
    {

        var queue = management
                .queue()
                .name(streamName)
                .stream()
                .queue()
                .declare();

        management.exchange(exchange)
                        .type(Management.ExchangeType.TOPIC)
                                .declare();

        management.binding().sourceExchange(exchange)
                .destinationQueue(streamName)
                .key(bindRoutingKey)
                .bind();

         return queue;

    }

}
