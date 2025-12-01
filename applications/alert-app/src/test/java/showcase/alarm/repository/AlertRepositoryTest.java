package showcase.alarm.repository;

import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import org.junit.jupiter.api.Test;
import showcase.alarm.domains.Alert;

import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

class AlertRepositoryTest {

    private final Alert alert = JavaBeanGeneratorCreator.of(Alert.class).create();
    private final AlertRepository subject = new AlertRepository();

    @Test
    void save() {
        subject.save(alert);

        assertThat(subject.findAll()).contains(alert);
    }

    @Test
    void duplicate() {
        subject.save(alert);
        subject.save(alert);

         AtomicInteger count = new AtomicInteger();

         subject.findAll().forEach( i -> count.getAndIncrement());

         assertThat(count.get()).isEqualTo(1);
    }
}