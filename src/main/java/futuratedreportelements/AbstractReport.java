package futuratedreportelements;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import sun.print.resources.serviceui;

@JsonDeserialize(as=SimpleReport.class)
public abstract class AbstractReport {
    protected ReportResult reportResult = ReportResult.PASS; //default to pass
    protected Date creationDate = new Date();
    //TODO: protected Status or int noOfTests
    //TODO: protected String commitId or commitDate;

    /**
     * Adds a new problem detail to an existing report
     * @param category                      General problem description e.g. unused import, bad indentation
     * @param filename                      Name of the file in which the problem was found
     * @param lineNumber                    Line number within the file where the problem was found
     * @param details                       More specific problem description e.g. java.io.StreamReader, expected
     *                                      12 space, found 16
     */
    public abstract void addDetail(String category, Severity severity, String filename, Integer lineNumber, String details);

    public AbstractReport() {};

    public Date getCreationDate() {
        return creationDate;
    }

    public ReportResult getReportResult() {
        return reportResult;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setReportResult(ReportResult reportResult) {
        this.reportResult = reportResult;
    }

    //TODO
}