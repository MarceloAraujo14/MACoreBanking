package com.maraujo.corebanking.core.repository;

import com.maraujo.corebanking.core.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Integer> {
    AccountEntity findByAccountNumber(Integer accountFrom);
}
