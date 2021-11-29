package banking.management.service;

import banking.management.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService implements ITransactionService{
    @Autowired
    private ITransactionService transactionService;

    @Override
    public List<Transaction> findAll() {
        return transactionService.findAll();
    }

    @Override
    public Transaction findById(Long id) {
        return transactionService.findById(id);
    }

    @Override
    public void save(Transaction transaction) {
        transactionService.save(transaction);
    }

    @Override
    public void remove(Long id) {
    }

}
