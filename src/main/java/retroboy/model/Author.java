package retroboy.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "authors")
public record Author(@Id Integer id, String userId, String cardId) {
}
