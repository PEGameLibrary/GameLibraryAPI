package ro.unibuc.hello.data.repository;

import ro.unibuc.hello.data.model.Game;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends MongoRepository<Game, Integer> {
}
