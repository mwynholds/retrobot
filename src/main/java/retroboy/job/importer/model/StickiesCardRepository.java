package retroboy.job.importer.model;

import java.util.stream.Stream;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StickiesCardRepository extends MongoRepository<StickiesCard, String> {
  Stream<StickiesCard> findAllBy();
}
