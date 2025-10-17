package retroboy.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@lombok.Data
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@Table(name = "users")
public class User {
  @Id private String id;
  private String email;
  private String source;
}
