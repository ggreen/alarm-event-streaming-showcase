package showcase.alarm.consumer;

import lombok.RequiredArgsConstructor;
import nyla.solutions.core.patterns.repository.memory.ListRepository;
import org.springframework.stereotype.Component;
import showcase.alarm.domains.Activity;

import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public class ActivityConsumer implements Consumer<Activity> {
    private final ListRepository<Activity> repository;

    @Override
    public void accept(Activity activity) {

    }
}
