package showcase.alarm.repository;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Repository;
import showcase.alarm.domains.Alert;

import java.util.ArrayList;
import java.util.List;

/**
 * Inmemory list of alerts
 * @author gregory green
 */
@Repository
public class AlertRepository {
    private final List<Alert> list = new ArrayList<>();

    public @Nullable Iterable<Alert> findAll() {
        return new ArrayList<>(list);
    }

    public void save(Alert alert) {
        if(!list.contains(alert))
            list.add(alert);
    }
}
