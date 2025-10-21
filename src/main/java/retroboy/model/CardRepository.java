package retroboy.model;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CardRepository extends CrudRepository<Card, String> {
  @Query("select * from cards limit :limit")
  List<Card> findSome(@Param("limit") int limit);

  @Query(value = """
        select *, ts_rank(body_tsv, plainto_tsquery('english', :query)) as rank
          from cards
         where body_tsv @@ plainto_tsquery('english', :query)
         order by rank desc, id desc
         limit :limit
      """)
  List<Card> findByQuery(@Param("query") String query, @Param("limit") int limit);
}