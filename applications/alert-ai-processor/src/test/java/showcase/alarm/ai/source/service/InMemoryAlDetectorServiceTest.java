package showcase.alarm.ai.source.service;

import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import nyla.solutions.core.patterns.repository.memory.ListRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import showcase.alarm.ai.source.service.ai.AlertsModelInference;
import showcase.streaming.domains.Activity;
import showcase.streaming.domains.Alert;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InMemoryAlDetectorServiceTest {

    private InMemoryAlDetectorService subject;
    @Mock
    private ListRepository<Activity> activityRepository;

    private final Activity activity = JavaBeanGeneratorCreator
            .of(Activity.class).create();

    @Mock
    private AlertsModelInference alertsModelinference;
    private final Alert alert = JavaBeanGeneratorCreator.of(Alert.class).create();

    @BeforeEach
    void setUp() {

    }


    @Test
    void givenHashMoreThanMin_when_detectAlerts_Then_Call() {

        subject = new InMemoryAlDetectorService(activityRepository, alertsModelinference,1);

        List<Alert> expected = List.of(alert);
        var activities = List.of(activity,activity);

        when(activityRepository.findAll())
                .thenReturn(activities);
        when(alertsModelinference.determineAlert(activities)).thenReturn(expected);

        var actual = subject.detectAlerts(activity);

        assertThat(actual).isNotEmpty();
    }

    @Test
    void givenHashLessThanMin_when_detectAlerts_Then_DoNotInfernece() {

        subject = new InMemoryAlDetectorService(activityRepository, alertsModelinference,10);

        List<Alert> expected = List.of(alert);
        var activities = List.of(activity,activity);

        when(activityRepository.findAll())
                .thenReturn(activities);
//        when(modelinference.determineActivities(activities)).thenReturn(expected);

        var actual = subject.detectAlerts(activity);

        assertThat(actual).isNull();
    }


    @Test
    void given_activities_with_alert_when_checkAlerts_then_Sends_Alerts() {

        subject = new InMemoryAlDetectorService(activityRepository, alertsModelinference,2);

        List<Activity> activities = List.of(activity,activity,activity);
        when(activityRepository.findAll()).thenReturn(activities);
        List<Alert> expected  = List.of(alert);
        when(alertsModelinference.determineAlert(any())).thenReturn(expected);

        List<Alert> alerts =  subject.checkForAlerts();

        assertThat(alerts).isNotEmpty();
        verify(activityRepository).deleteAll();

    }
}