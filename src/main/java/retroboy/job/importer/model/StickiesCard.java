package retroboy.job.importer.model;

import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "cards")
public record StickiesCard(@Id String id, String text, String creator, Set<String> authors) implements StickiesIdentifiable {
}
