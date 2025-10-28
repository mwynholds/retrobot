package retroboy.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "users")
public record User(@Id String id, String email, String source, LocalDateTime createdAt, LocalDateTime updatedAt) {
}
