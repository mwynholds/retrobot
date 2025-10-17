package retroboy.job.importer.model;

import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@lombok.Data
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@Document(collection = "cards")
public class StickiesCard implements StickiesIdentifiable {
  @Id private String id;
  private String text;
  private String creator;
  private Set<String> authors;
}
