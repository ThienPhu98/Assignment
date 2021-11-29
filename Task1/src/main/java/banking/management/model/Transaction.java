package banking.management.model;

import javax.persistence.*;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private double amount;
    private long depositorId;
    private long receiverId;
    private double interested;

    public Transaction(){
    }

    public Transaction(double amount, long depositorId, long receiverId, double interested) {
        this.amount = amount;
        this.depositorId = depositorId;
        this.receiverId = receiverId;
        this.interested = interested;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public long getDepositorId() {
        return depositorId;
    }

    public void setDepositorId(long depositorId) {
        this.depositorId = depositorId;
    }

    public long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(long receiverId) {
        this.receiverId = receiverId;
    }

    public double getInterested() {
        return interested;
    }

    public void setInterested(double interested) {
        this.interested = interested;
    }
}
