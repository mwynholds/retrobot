package retroboy.job.importer;

import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@lombok.Data
@lombok.NoArgsConstructor
@Document(collection = "cards")
public class StickiesCard {
  @Id
  private String id;
  private String body;
  private String creator;
  private Set<String> authors;
}
