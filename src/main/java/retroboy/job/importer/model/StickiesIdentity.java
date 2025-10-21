package retroboy.job.importer.model;

import java.util.Date;

import org.springframework.data.annotation.Id;

public record StickiesIdentity(@Id String id, String email, String source, Date updated) {
}
