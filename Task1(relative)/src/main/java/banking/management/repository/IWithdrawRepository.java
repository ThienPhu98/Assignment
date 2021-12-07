package banking.management.repository;


import banking.management.model.Withdraw;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IWithdrawRepository extends PagingAndSortingRepository<Withdraw, Long> {
}
