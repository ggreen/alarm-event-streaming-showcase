package showcase.alarm.consumer;

import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import showcase.alarm.domains.Alert;
import showcase.alarm.repository.AlertRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AlertConsumerTest {

    @Mock
    private AlertRepository repository;

    private AlertConsumer subject;
    private final Alert alert = JavaBeanGeneratorCreator.of(Alert.class).create();

    @BeforeEach
    void setUp() {
        subject = new AlertConsumer(repository);
    }

    @Test
    void accept() {

        subject.accept(alert);

        verify(repository).save(alert);
    }
}