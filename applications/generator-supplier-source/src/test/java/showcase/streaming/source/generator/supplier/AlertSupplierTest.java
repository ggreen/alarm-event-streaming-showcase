package showcase.streaming.source.generator.supplier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static nyla.solutions.core.util.Debugger.println;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AlertSupplierTest {

    private final String account = "junit";
    private final String[] events = {"junit","testing"};

    private AlertSupplier subject;
    private int idSequence = 0;

    @BeforeEach
    void setUp() {
         subject = new AlertSupplier(idSequence,account,events);
    }

    @Test
    void get() {
        var actual = subject.get();
        println(actual);
        assertThat(actual).isNotNull();
    }

}