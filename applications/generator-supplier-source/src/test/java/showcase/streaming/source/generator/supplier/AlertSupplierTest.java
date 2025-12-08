package showcase.streaming.source.generator.supplier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import showcase.streaming.source.generator.properties.AlertProperties;

import static java.util.Arrays.asList;
import static nyla.solutions.core.util.Debugger.println;
import static org.assertj.core.api.Assertions.assertThat;

class AlertSupplierTest {


    private AlertSupplier subject;

    @BeforeEach
    void setUp() {
        int idSequence = 0;
        String account = "junit";
        AlertProperties alertProperties = AlertProperties.builder()
                .account(account)
                .idStartSequence(idSequence)
                .events(asList("junit", "test"))
                .levels(asList("High", "Low"))
                .build();

         subject = new AlertSupplier(alertProperties);
    }

    @Test
    void get() {
        var actual = subject.get();
        println(actual);
        assertThat(actual).isNotNull();
    }

}