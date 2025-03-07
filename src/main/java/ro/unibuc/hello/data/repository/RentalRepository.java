package ro.unibuc.hello.data.repository;

import ro.unibuc.hello.data.model.Rental;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RentalRepository extends MongoRepository<Rental, String> {
    List<Rental> findByUserId(String userId);
    List<Rental> findByGameId(String gameId);
}
