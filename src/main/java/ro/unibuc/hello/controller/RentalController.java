package ro.unibuc.hello.controller;

import ro.unibuc.hello.data.model.Rental;
import ro.unibuc.hello.service.RentalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping("/rentals")
public class RentalController {

    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

@PostMapping
public ResponseEntity<?> rentGame(@RequestBody Rental rentalRequest) {
    try {
        return ResponseEntity.ok(rentalService.rentGame(
                rentalRequest.getUserId(), 
                rentalRequest.getGameId(), 
                rentalRequest.getRentalDays()
        ));
    } catch (RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}


   @DeleteMapping("/{rentalId}")
    public ResponseEntity<?> returnGame(@PathVariable String rentalId) {
        try {
            return rentalService.returnGame(rentalId)
                    .map(r -> ResponseEntity.ok("Joc returnat cu succes!"))
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Adăugare endpoint pentru listarea tuturor închirierilor
    @GetMapping("/all")
    public ResponseEntity<List<Rental>> getAllRentals() {
        return ResponseEntity.ok(rentalService.getAllRentals());
    }

    @GetMapping("/DeleteAll")
    public ResponseEntity<String> deleteAllRentals() {
        rentalService.deleteAllRentals();
        return ResponseEntity.ok("Toate închirierile au fost șterse!");
    }
}
