package retroboy.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "cards")
public record Card(@Id String id, String body, String creatorId) {
}
