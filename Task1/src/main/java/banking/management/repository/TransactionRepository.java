package banking.management.repository;

import banking.management.model.Customer;
import banking.management.model.Transaction;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
@Transactional
public class TransactionRepository implements ITransactionRepository{
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Transaction> findAll() {
        TypedQuery<Transaction> query = em.createQuery("select c from Transaction c", Transaction.class);
        return query.getResultList();
    }

    @Override
    public Transaction findById(Long id) {
        TypedQuery<Transaction> query = em.createQuery("select c from Transaction c where  c.id=:id", Transaction.class);
        query.setParameter("id", id);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public void save(Transaction transaction) {
        if (transaction.getId() != null) {
            em.merge(transaction);
        } else {
            em.persist(transaction);
        }
    }

    @Override
    public boolean remove(Long id) {
        return false;
    }
}
