package retroboy.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@lombok.Data
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@Table(name = "authors")
public class Author {
  @Id private int id;
  private String userId;
  private String cardId;
}
