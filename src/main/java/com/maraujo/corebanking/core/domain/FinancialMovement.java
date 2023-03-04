package com.maraujo.corebanking.core.domain;

import com.maraujo.corebanking.core.entity.TransferEntity;
import com.maraujo.corebanking.core.repository.AccountRepository;
import com.maraujo.corebanking.core.repository.TransferRepository;
import com.maraujo.corebanking.core.request.FinancialMovementRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@Service
public class FinancialMovement {

    private final TransferRepository transferRepository;
    private final AccountRepository accountRepository;

    public TransferEntity transfer(FinancialMovementRequest financialMovementRequest) {

        log.info("m=transfer, state=NEW, financialMovimentRequest={}", financialMovementRequest);

        Integer accountFromNumber = financialMovementRequest.getAccountFrom();
        Integer accountToNumber = financialMovementRequest.getAccountTo();
        try {
            var accountFrom = Optional.of(accountRepository.findByAccountNumber(accountFromNumber)).orElseThrow(() -> new EntityNotFoundException("Account" + accountFromNumber + " not found"));
            var accountTo = Optional.of(accountRepository.findByAccountNumber(accountToNumber)).orElseThrow(() -> new EntityNotFoundException("Account" + accountToNumber + " not found"));

            var balanceFrom = accountFrom.getBalance();
            var balanceFromResult = balanceFrom - financialMovementRequest.getAmount();
            if (balanceFromResult < 0) {
                throw new IllegalStateException("Insuficient Balance");
            }
            accountFrom.setBalance(balanceFromResult);

            var balanceTo = accountTo.getBalance();
            var balanceToResult = balanceTo + financialMovementRequest.getAmount();

            accountTo.setBalance(balanceToResult);

            accountFrom.setLock(false);

            accountRepository.saveAll(List.of(accountFrom, accountTo));

            log.info("m=transfer, state=SUCCESS");
        }catch (Exception ex){
            log.error("m=transfer, state=FAILURE, error={}", ex.getMessage());
            throw ex;
        }

        return transferRepository.save(
                TransferEntity.builder()
                        .operationTransferId(financialMovementRequest.getOperationId())
                        .accountFrom(accountFromNumber)
                        .accountTo(accountToNumber)
                        .amount(financialMovementRequest.getAmount())
                .build());
    }

}
