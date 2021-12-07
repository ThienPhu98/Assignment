package banking.management.model;

import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@Table(name = "customers")
public class Customer {
    @Id
    @NotNull
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @OneToMany(targetEntity = Transfer.class)
    private List<Transfer> transferList;

    @OneToMany(targetEntity = Deposit.class)
    private List<Deposit> depositList;

    @OneToMany(targetEntity = Withdraw.class)
    private List<Withdraw> withdrawList;

    @DateTimeFormat
    String created_at;

    long created_by;

    @ColumnDefault("0")
    byte deleted;

    @DateTimeFormat
    String updated_at;

    long updated_by;

    @Size(max = 255)
    String address;

    @ColumnDefault("0")
    @PositiveOrZero
    @Digits(integer = 12, fraction = 0)
    BigDecimal balance;

    @Size(max = 50)
    String email;

    @Size(max = 255)
    String full_name;

    @Size(min = 10, max = 11)
    String phone;

    public Customer(String address, String email, String full_name, String phone) {
        this.address = address;
        this.email = email;
        this.full_name = full_name;
        this.phone = phone;
    }

    public Customer() {
    }
}
