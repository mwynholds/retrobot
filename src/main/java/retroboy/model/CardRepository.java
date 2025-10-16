package retroboy.model;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

public interface CardRepository extends CrudRepository<Card, UUID> {

}