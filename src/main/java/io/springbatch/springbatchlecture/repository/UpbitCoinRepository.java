package io.springbatch.springbatchlecture.repository;

import io.springbatch.springbatchlecture.domain.UpbitCoinHistory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class UpbitCoinRepository {
    @PersistenceContext
    private EntityManager em;

    public void save(UpbitCoinHistory upbitCoinHistory) {
        em.persist(upbitCoinHistory);
    }
}
