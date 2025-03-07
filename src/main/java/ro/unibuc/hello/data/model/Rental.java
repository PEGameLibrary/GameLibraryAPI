package ro.unibuc.hello.data.model;
import com.fasterxml.jackson.annotation.JsonFormat;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

@Document(collection = "rentals")
public class Rental {

    @Id
    private String id;
    private String userId;  // ID-ul utilizatorului care a închiriat jocul
    private String gameId;  // ID-ul jocului închiriat

   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    private Instant rentalDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    private Instant returnDate;

    private double penaltyFee; // Penalizare acumulată

    private int rentalDays;

    public Rental() {
        this.rentalDate = Instant.now();
    }

    public Rental(String userId, String gameId, int rentalDays) {
        this.userId = userId;
        this.gameId = gameId;
        this.rentalDays = rentalDays;
        this.rentalDate = Instant.now();
        this.returnDate = rentalDate.plusSeconds(rentalDays * 86400L);
        this.penaltyFee = 0.0;
    }

    
 public boolean isOverdue() {
        return Instant.now().isAfter(returnDate);
    }

    public void applyLateFee(double dailyFee) {
        if (isOverdue()) {
            this.penaltyFee += dailyFee;
        }
    }

    // Convertește Instant în LocalDate doar pentru afisaj (dacă e necesar)
    public LocalDate getRentalDateAsLocalDate() {
        return LocalDate.ofInstant(rentalDate, ZoneId.systemDefault());
    }

    public LocalDate getReturnDateAsLocalDate() {
        return LocalDate.ofInstant(returnDate, ZoneId.systemDefault());
    }

    // Getters și Setters
    public double getPenaltyFee() { return penaltyFee; }
    public void setPenaltyFee(double penaltyFee) { this.penaltyFee = penaltyFee; }

    public String getUserId() { return userId; }
    public String getGameId() { return gameId; }

    public Instant getRentalDate() { return rentalDate; }
    public void setRentalDate(Instant rentalDate) { this.rentalDate = rentalDate; }

    public Instant getReturnDate() { return returnDate; }
    public void setReturnDate(Instant returnDate) { this.returnDate = returnDate; }

    public int getRentalDays() { return rentalDays; }
    public void setRentalDays(int rentalDays) { this.rentalDays = rentalDays; }


}

