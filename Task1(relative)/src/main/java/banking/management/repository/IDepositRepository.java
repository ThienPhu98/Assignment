package banking.management.repository;

import banking.management.model.Deposit;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IDepositRepository extends PagingAndSortingRepository<Deposit, Long> {
}
