package io.springbatch.springbatchlecture.repository;

import io.springbatch.springbatchlecture.batch.api.UpbitCoinHistoryApi;
import io.springbatch.springbatchlecture.domain.UpbitCoinHistory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class UpbitApiCoinRepository {
    @PersistenceContext
    private EntityManager em;

    public void save(UpbitCoinHistory upbitCoinHistory) {
        em.persist(upbitCoinHistory);
    }

    public UpbitCoinHistoryApi find (Long coinId){
        return em.find(UpbitCoinHistoryApi.class, coinId);
    }
}
