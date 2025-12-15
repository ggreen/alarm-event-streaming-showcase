package showcase.alarm.ai.source.service;

import lombok.extern.slf4j.Slf4j;
import nyla.solutions.core.patterns.repository.memory.ListRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import showcase.alarm.ai.source.service.ai.AlertsModelInference;
import showcase.streaming.domains.Activity;
import showcase.streaming.domains.Alert;

import java.util.List;


@Service
@Slf4j
public class InMemoryAlDetectorService implements AlertDetectorService{
    private final ListRepository<Activity> activityRepository;
    private final AlertsModelInference modelInference;
    private final int batchCount;

    public InMemoryAlDetectorService(ListRepository<Activity> activityRepository,
                                     AlertsModelInference modelInference,
                                     @Value("${alerts.inference.batch}")
                                     int batchCount) {
        this.activityRepository = activityRepository;
        this.modelInference = modelInference;
        this.batchCount = batchCount;
    }

    @Override
    public List<Alert> detectAlerts(Activity activity) {
        log.info("Saving activity: {}",activity);

        activityRepository.save(activity);
        log.info("Detecting alerts for activity: {}",activity);

        return checkForAlerts();
    }

    @Override
    public synchronized List<Alert> checkForAlerts() {
        log.info("Checking for alerts");
        var activities = (List<Activity>) activityRepository.findAll();
        log.info("Activities: {}",activities);

        if((activities).size() > batchCount)
        {
            var alerts = this.modelInference.determineAlert(activities);

            if(alerts != null && !alerts.isEmpty())
            {
                log.info("Deleting activities from repository");
                activityRepository.deleteAll();
            }

            return alerts;
        }

        return null;
    }
}
