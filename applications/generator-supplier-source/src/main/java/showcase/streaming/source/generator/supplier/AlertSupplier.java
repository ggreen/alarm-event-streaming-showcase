package showcase.streaming.source.generator.supplier;

import nyla.solutions.core.util.Digits;
import nyla.solutions.core.util.Text;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import showcase.streaming.domains.Alert;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.function.Supplier;

@Component
public class AlertSupplier implements Supplier<Alert> {

    private int idSequence;
    private final Digits digits = new Digits();
    private final String [] levels = {"Critical", "High", "Medium","Low"};
    private final String account;
    private final String[] events;

    public AlertSupplier(@Value("${generator.idSequence:0}") int idSequence,
                         @Value("${generator.account}") String account,
                         @Value("${generator.events}") String[] events) {
        this.idSequence = idSequence;
        this.account = account;
        this.events = events;
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
        var index = digits.generateInteger(0,events.length-1);
        return events[index];
    }

    private String time() {
        return Text.format().formatDate("hh:mm a ss S ", new Date());
    }


    private String id() {
        return String.valueOf(idSequence++);
    }

    private String level() {
        var levelIndex = digits.generateInteger(0,levels.length-1);
        return levels[levelIndex];
    }
}
