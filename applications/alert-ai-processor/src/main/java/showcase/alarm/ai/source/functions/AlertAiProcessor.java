package showcase.alarm.ai.source.functions;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import showcase.alarm.ai.source.service.AlertDetectorService;
import showcase.streaming.domains.Activity;
import showcase.streaming.domains.Alert;

import java.util.List;
import java.util.function.Function;

/**
 * Alert AI processor
 */
@Component
@RequiredArgsConstructor
public class AlertAiProcessor implements Function<Activity,List<Message<Alert>>> {

    private final AlertDetectorService detectorService;

    @Override
    public List<Message<Alert>> apply(Activity activity) {
        var alerts = detectorService.detectAlerts(activity);

        if(alerts != null && !alerts.isEmpty())
            return alerts.stream().map( alert -> MessageBuilder.withPayload(alert).build())
                    .toList();

        return null;
    }
}
