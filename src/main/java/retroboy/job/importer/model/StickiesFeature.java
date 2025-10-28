package retroboy.job.importer.model;

import java.time.LocalDateTime;

public record StickiesFeature(String featureName, LocalDateTime created, LocalDateTime updated) {
}
