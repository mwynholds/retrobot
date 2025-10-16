package retroboy.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@lombok.Data
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@Table(name = "cards")
public class Card {
  @Id
  private String id;
  private String body;
  private String creatorId;
}
