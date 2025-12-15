package showcase.alarm.ai.source.service;

import nyla.solutions.core.patterns.repository.memory.ListRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import showcase.alarm.ai.source.service.ai.AlertsModelInference;
import showcase.streaming.domains.Activity;
import showcase.streaming.domains.Alert;

import java.util.List;


@Service
public class InMemoryAlDetectorService implements AlertDetectorService{
    private final ListRepository<Activity> repository;
    private final AlertsModelInference modelInference;
    private final int batchCount;

    public InMemoryAlDetectorService(ListRepository<Activity> repository,
                                     AlertsModelInference modelInference,
                                     @Value("${alerts.inference.batch}")
                                     int batchCount) {
        this.repository = repository;
        this.modelInference = modelInference;
        this.batchCount = batchCount;
    }

    @Override
    public List<Alert> detectAlerts(Activity activity) {
        repository.save(activity);

        return checkForAlerts();
    }

    @Override
    public List<Alert> checkForAlerts() {
        var activities = (List<Activity>) repository.findAll();

        if((activities).size() > batchCount)
        {
            var alerts = this.modelInference.determineAlert(activities);

            if(alerts != null && !alerts.isEmpty())
                repository.deleteAll();

            return alerts;
        }

        return null;
    }
}
