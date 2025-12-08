package showcase.streaming.source.generator.supplier;

import nyla.solutions.core.util.Digits;
import nyla.solutions.core.util.Text;
import org.springframework.stereotype.Component;
import showcase.streaming.domains.Alert;
import showcase.streaming.source.generator.properties.AlertProperties;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.function.Supplier;

@Component
public class AlertSupplier implements Supplier<Alert> {

    private final String account;
    private final List<String> events;
    private int idSequence;
    private final Digits digits = new Digits();
    private final List<String> levels;

    public AlertSupplier(AlertProperties alertProperties) {
        this.idSequence = alertProperties.getIdStartSequence();
        this.account = alertProperties.getAccount();
        this.events = alertProperties.getEvents();
        this.levels = alertProperties.getLevels();
    }

    @Override
    public Alert get() {

        return Alert.builder().id(id())
                .level(level())
                .account(account)
                .time(time())
                .dateTime(LocalDateTime.now())
                .event(event())
                .build();
    }

    private String event() {
        var index = digits.generateInteger(0,events.size()-1);
        return events.get(index);
    }

    private String time() {
        return Text.format().formatDate("hh:mm a ss S ", new Date());
    }


    private String id() {
        return String.valueOf(idSequence++);
    }

    private String level() {
        var levelIndex = digits.generateInteger(0,levels.size()-1);
        return levels.get(levelIndex);
    }
}
