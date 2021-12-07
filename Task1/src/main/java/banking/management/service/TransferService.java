package banking.management.service;

import banking.management.model.Transfer;
import banking.management.repository.ITransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransferService implements ITransferService {
    @Autowired
    private ITransferRepository transferRepository;

    @Override
    public List<Transfer> findAll() {
        return transferRepository.findAll();
    }

    @Override
    public Transfer findById(Long id) {
        return transferRepository.findById(id);
    }

    @Override
    public void save(Transfer transaction) {
        transferRepository.save(transaction);
    }

    @Override
    public void remove(Long id) {
    }

}
