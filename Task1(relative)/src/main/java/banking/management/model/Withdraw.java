package banking.management.model;

import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;


@Entity
@Data
@Table(name = "withdraws")
public class Withdraw {
    @Id
    @NotNull
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @DateTimeFormat
    String created_at;

    long created_by;

    @ColumnDefault("0")
    byte deleted;

    @DateTimeFormat
    String updated_at;

    long updated_by;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    Customer customer;

    @PositiveOrZero
    @Digits(integer = 12, fraction = 0)
    BigDecimal transaction_amount;

    public Withdraw(){
    }

}
