package futuratedreportelements;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.LinkedList;
import java.util.List;

public class SimpleReport extends AbstractReport {
    private LinkedList<SimpleReportItem> items = new LinkedList<>();

    public SimpleReport() {};

    public LinkedList<SimpleReportItem> getItems() {
        return items;
    }

    public void setItems(LinkedList<SimpleReportItem> items) {
        this.items = items;
    }

    /** {@inheritDoc} */
    @Override
    @JsonIgnore
    public void addDetail(String category, Severity severity, String filename, Integer lineNumber, String details) {
        items.add(new SimpleReportItem(category, severity, filename, lineNumber, details));
        if (severity == Severity.ERROR && lineNumber != null) {
            this.reportResult = ReportResult.FAIL;
        }
    }
}
