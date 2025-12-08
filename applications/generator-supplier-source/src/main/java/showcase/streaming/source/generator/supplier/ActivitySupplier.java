package showcase.streaming.source.generator.supplier;

import nyla.solutions.core.io.csv.CsvReader;
import nyla.solutions.core.util.Digits;
import nyla.solutions.core.util.Text;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import showcase.streaming.domains.Activity;

import java.util.Date;
import java.util.function.Supplier;

@Component
public class ActivitySupplier implements Supplier<Activity> {
    private int idSequence;
    private final Digits digits = new Digits();

    private final CsvReader csvReader;

    public ActivitySupplier(@Value("${generator.activity.id.start:0}") int idSequence,
                            CsvReader csvReader) {
        if(csvReader.isEmpty())
            throw new IllegalArgumentException("Csv Reader size must be > 0");

        this.idSequence = idSequence;
        this.csvReader = csvReader;
    }

    @Override
    public Activity get() {

        if(idSequence >= csvReader.size())
            idSequence = 0;

        var row = csvReader.row(idSequence++);

        return Activity.builder()
                .id(id())
                .time(Text.format().formatDate("hh:mm a ss S ", new Date()))
                .activity(row.get(0))
                .icon(row.get(1))
                .build();
    }

    private String id() {
        return String.valueOf(idSequence++);
    }
}
