package showcase.alarm.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import showcase.alarm.domains.Alert;
import showcase.alarm.repository.AlertRepository;

import java.util.function.Consumer;

@Component
@Slf4j
@RequiredArgsConstructor
public class AlertConsumer implements Consumer<Alert> {

    private final AlertRepository repository;

    @Override
    public void accept(Alert alert) {
        log.info("processing alert: {}",alert);
        repository.save(alert);
    }
}
