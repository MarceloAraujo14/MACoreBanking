package com.maraujo.corebanking.core.service;

import com.maraujo.corebanking.core.entity.AccountEntity;
import com.maraujo.corebanking.core.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountEntity findByAccountNumber(Integer accountNumber){
        return Optional.of(accountRepository.findByAccountNumber(accountNumber))
                .orElseThrow(() -> new EntityNotFoundException("Account" + accountNumber + " not found"));
    }

    public List<AccountEntity> saveAll(Iterable<AccountEntity> accounts){
        return accountRepository.saveAll(accounts);
    }

    public void deleteAll(){
        accountRepository.deleteAll();
    }

}
