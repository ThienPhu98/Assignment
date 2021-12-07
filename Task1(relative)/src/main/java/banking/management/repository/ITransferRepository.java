package banking.management.repository;

import banking.management.model.Transfer;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITransferRepository extends PagingAndSortingRepository<Transfer, Long> {
}
