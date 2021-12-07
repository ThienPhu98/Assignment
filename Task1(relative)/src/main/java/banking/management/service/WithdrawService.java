package banking.management.service;

import banking.management.model.Withdraw;
import banking.management.repository.IWithdrawRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WithdrawService implements IWithdrawService{

    @Autowired
    IWithdrawRepository withdrawRepository;

    @Override
    public Iterable<Withdraw> findAll() {
        return withdrawRepository.findAll();
    }

    @Override
    public Optional<Withdraw> findById(Long id) {
        return withdrawRepository.findById(id);
    }

    @Override
    public void save(Withdraw withdraw) {
        withdrawRepository.save(withdraw);
    }

    @Override
    public void remove(Long id) {

    }
}
