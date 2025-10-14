package retroboy.job.importer;

import java.util.stream.Stream;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StickiesUserRepo extends MongoRepository<StickiesUser, String> {
  Stream<StickiesUser> findAllBy();
}
