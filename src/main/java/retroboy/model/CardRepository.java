package retroboy.model;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CardRepository extends CrudRepository<Card, String> {
  @Query("SELECT * FROM cards  LIMIT :limit")
  List<Card> findSome(@Param("limit") int limit);
}