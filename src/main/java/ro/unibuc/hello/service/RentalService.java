package ro.unibuc.hello.service;

import ro.unibuc.hello.data.model.Rental;
import ro.unibuc.hello.data.repository.RentalRepository;
import ro.unibuc.hello.data.repository.UserRepository;
import ro.unibuc.hello.data.repository.GameRepository;
import ro.unibuc.hello.data.model.Game;
import ro.unibuc.hello.data.model.User;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class RentalService {

    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private static final double DAILY_LATE_FEE = 2.5; // Penalizare zilnică

    public RentalService(RentalRepository rentalRepository, UserRepository userRepository, GameRepository gameRepository) {
        this.rentalRepository = rentalRepository;
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
    }

    public Rental rentGame(String userId, String gameId, int rentalDays) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Game> gameOpt = gameRepository.findById(gameId);

        if (userOpt.isEmpty() || gameOpt.isEmpty()) {
            throw new RuntimeException("Utilizatorul sau jocul nu există!");
        }

        User user = userOpt.get();
        Game game = gameOpt.get();

        // Verificăm dacă user are închirieri întârziate
        List<Rental> overdueRentals = rentalRepository.findByUserId(userId)
                .stream().filter(Rental::isOverdue).toList();
        if (!overdueRentals.isEmpty()) {
            throw new RuntimeException("Nu poți închiria până nu returnezi jocurile restante!");
        }

        // Verificăm balanța utilizatorului
        if (user.getBalance() < game.getPrice() * rentalDays) {
            throw new RuntimeException("Balanță insuficientă pentru a închiria acest joc!");
        }

        // Scădem din balanță
        user.deductBalance(game.getPrice() * rentalDays);
        userRepository.save(user);

        // Creăm închirierea
        Rental rental = new Rental(userId, gameId, rentalDays);
        rental.setRentalDate(Instant.now());
        rental.setReturnDate(Instant.now().plusSeconds((long) rentalDays * 86400)); // Calculăm return date
        System.out.println("DEBUG: rentalDate=" + rental.getRentalDate() + " returnDate=" + rental.getReturnDate());

        return rentalRepository.save(rental);
    }

    public Optional<Rental> returnGame(String rentalId) {
        Optional<Rental> rentalOpt = rentalRepository.findById(rentalId);
        if (rentalOpt.isEmpty()) {
            return Optional.empty();
        }

        Rental rental = rentalOpt.get();
        Optional<User> userOpt = userRepository.findById(rental.getUserId());

        if (userOpt.isEmpty()) {
            throw new RuntimeException("Utilizatorul nu există!");
        }

        User user = userOpt.get();

        // Aplicăm penalizare dacă există întârzieri
        if (rental.isOverdue()) {
            double totalLateFee = rental.getPenaltyFee();
            user.deductBalance(totalLateFee);
            userRepository.save(user);
        }

        rentalRepository.delete(rental);
        return Optional.of(rental);
    }

    // Programare automată pentru aplicarea penalizărilor zilnic la ora 00:00
    @Scheduled(cron = "0 0 0 * * ?")
    public void applyLateFees() {
        List<Rental> rentals = rentalRepository.findAll();
        rentals.forEach(rental -> {
            rental.applyLateFee(DAILY_LATE_FEE);
            rentalRepository.save(rental);
        });
    }

    // Trimite notificări utilizatorilor cu 2 zile înainte de expirare
    @Scheduled(cron = "0 0 12 * * ?") // Se execută zilnic la ora 12:00
    public void sendRentalReminders() {
        List<Rental> rentals = rentalRepository.findAll();
        rentals.forEach(rental -> {
            if (rental.getReturnDate().minusSeconds(2 * 86400L).isBefore(Instant.now())) {
                System.out.println("Reminder: Jocul " + rental.getGameId() + " trebuie returnat în 2 zile!");
            }
        });
    }

    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }

    public void deleteAllRentals() {
        rentalRepository.deleteAll();
    }
}
