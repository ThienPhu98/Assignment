package banking.management.repository;

import banking.management.model.Transfer;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class TransferRepository implements ITransferRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Transfer> findAll() {
        TypedQuery<Transfer> query = em.createQuery("select c from Transfer c", Transfer.class);
        return query.getResultList();
    }

    @Override
    public Transfer findById(Long id) {
        TypedQuery<Transfer> query = em.createQuery("select c from Transfer c where  c.id=:id", Transfer.class);
        query.setParameter("id", id);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public void save(Transfer transfer) {
        if (transfer.getId() != null) {
            em.merge(transfer);
        } else {
            em.persist(transfer);
        }
    }

    @Override
    public boolean remove(Long id) {
        return false;
    }
}
