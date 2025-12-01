package showcase.alarm.controller;

import nyla.solutions.core.patterns.creational.generator.JavaBeanGeneratorCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import showcase.alarm.domains.Alert;
import showcase.alarm.repository.AlertRepository;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AlertControllerTest {

    private AlertController subject;
    private final Alert alert = JavaBeanGeneratorCreator.of(Alert.class).create();

    @Mock
    private AlertRepository repository;

    @BeforeEach
    void setUp() {
        subject = new AlertController(repository);
    }

    @Test
    void accounts() {
        subject.saveAlert(alert);

        verify(repository).save(any());
    }
}