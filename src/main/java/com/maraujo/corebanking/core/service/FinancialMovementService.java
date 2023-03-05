package com.maraujo.corebanking.core.service;

import com.maraujo.corebanking.core.entity.TransferEntity;
import com.maraujo.corebanking.core.repository.TransferRepository;
import com.maraujo.corebanking.core.request.TransferRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Service
public class FinancialMovementService {

    private final TransferRepository transferRepository;
    private final AccountService accountService;

    public TransferEntity transfer(TransferRequest transferRequest) {

        log.info("m=transferRequest, transferRequest={}, state=NEW", transferRequest);

        Integer accountFromNumber = transferRequest.getAccountFrom();
        Integer accountToNumber = transferRequest.getAccountTo();
        try {
            var accountFrom = accountService.findByAccountNumber(accountFromNumber);
            var accountTo =  accountService.findByAccountNumber(accountToNumber);

            var balanceFrom = accountFrom.getBalance();
            var balanceFromResult = balanceFrom - transferRequest.getAmount();
            if (balanceFromResult < 0) {
                throw new IllegalStateException("Insuficient Balance");
            }
            accountFrom.setBalance(balanceFromResult);

            var balanceTo = accountTo.getBalance();
            var balanceToResult = balanceTo + transferRequest.getAmount();

            accountTo.setBalance(balanceToResult);

            accountFrom.setLock(false);

            accountService.saveAll(List.of(accountFrom, accountTo));

            log.info("m=transferRequest,transferRequest={} , state=SUCCESS", transferRequest);
        }catch (Exception ex){
            log.error("m=transferRequest, transferRequest={}, error={}, state=FAILURE", transferRequest, ex.getMessage());
            throw ex;
        }

        return transferRepository.save(
                TransferEntity.builder()
                        .operationTransferId(transferRequest.getOperationId())
                        .accountFrom(accountFromNumber)
                        .accountTo(accountToNumber)
                        .amount(transferRequest.getAmount())
                .build());
    }

}
