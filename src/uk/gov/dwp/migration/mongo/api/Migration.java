package uk.gov.dwp.migration.mongo.api;

import java.time.Instant;

public class Migration {

    private final Instant started;
    private Long totalDocumentsToMigrate;
    private Long recordsMigrated;
    private Instant completed;

    public Migration(Instant started) {
        this.started = started;
    }

    public static Migration start() {
        return new Migration(Instant.now());
    }

    public void complete() {
        this.completed = Instant.now();
    }

    public void setTotalDocumentsToMigrate(long totalDocumentsToMigrate) {
        this.totalDocumentsToMigrate = totalDocumentsToMigrate;
        this.recordsMigrated = 0L;
    }

    public void recordMigrated() {
        recordsMigrated++;
    }

    public Instant getStarted() {
        return started;
    }

    public Long getTotalDocumentsToMigrate() {
        return totalDocumentsToMigrate;
    }

    public Long getRecordsMigrated() {
        return recordsMigrated;
    }

    public Instant getCompleted() {
        return completed;
    }
}
