package ro.unibuc.hello.data.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonFormat;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

@Document(collection = "users")
public class User {

    @Id
    private String id;
    private String fName;
    private String lName;
    private String userName;
    private String password;
    private LocalDate birthDate;
    private String email;
    private String phoneNumber;
    private LocalDate registrationDate; 
    private double balance; 


    public User() {
    this.registrationDate = LocalDate.now();
    this.balance = 0.0;
}


    public User(String fName, String lName, String userName, String password,
                LocalDate birthDate, String email, String phoneNumber, double balance) {
        this.fName = fName;
        this.lName = lName;
        this.userName = userName;
        this.password = password;
        this.birthDate = birthDate;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.registrationDate = LocalDate.now();
        this.balance = balance;

    }


    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getfName() {
        return fName;
    }
    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }
    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }
    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }
    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

     public boolean hasEnoughBalance(double amount) {
        return balance >= amount;
    }

    public void deductBalance(double amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
        }
    }

    public void addBalance(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }

}
