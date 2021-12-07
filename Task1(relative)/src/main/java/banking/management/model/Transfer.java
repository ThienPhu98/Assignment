package banking.management.model;

import lombok.Data;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Entity
@Data
@Table(name = "transfers")
public class Transfer {
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
    int fees;

    @PositiveOrZero
    @Digits(integer = 12, fraction = 0)
    BigDecimal fees_amount;

    @PositiveOrZero
    @Digits(integer = 12, fraction = 0)
    BigDecimal transaction_amount;

    @PositiveOrZero
    @Digits(integer = 12, fraction = 0)
    BigDecimal transfer_amount;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "recipient_id", referencedColumnName = "id")
    Customer recipient;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "id")
    Customer sender;

    public Transfer(){
    }

    public Transfer(String created_at, Long created_by, String updated_at, long updated_by, int fees, BigDecimal fees_amount, BigDecimal transaction_amount, BigDecimal transfer_amount, Customer recipient, Customer sender) {
        this.created_at = created_at;
        this.created_by = created_by;
        this.updated_at = updated_at;
        this.updated_by = updated_by;
        this.fees = fees;
        this.fees_amount = fees_amount;
        this.transaction_amount = transaction_amount;
        this.transfer_amount = transfer_amount;
        this.recipient = recipient;
        this.sender = sender;
    }
}
