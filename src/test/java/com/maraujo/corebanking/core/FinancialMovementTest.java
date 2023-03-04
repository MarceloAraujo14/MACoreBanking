package com.maraujo.corebanking.core;

import com.maraujo.corebanking.core.domain.FinancialMovement;
import com.maraujo.corebanking.core.entity.AccountEntity;
import com.maraujo.corebanking.core.repository.AccountRepository;
import com.maraujo.corebanking.core.request.FinancialMovementRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author maraujo
 *
 *
 */

@SpringBootTest
@ActiveProfiles("test")
class FinancialMovementTest {

    @Autowired
    FinancialMovement financialMovement;
    @Autowired
    AccountRepository accountRepository;

    @AfterEach
    void tearDown() {
        accountRepository.deleteAll();
    }

    /**
     * Deve realizar a movimentação do saldo entre a conta de origem e a conta de destino
     */
    @Test
    void should_transfer_from_origin_to_destiny_account(){
        //given
        Long amount = 150L;
        int accountFromNumber = 443520;
        int accountToNumber = 302210;
        var accountFrom = AccountEntity.builder().accountNumber(accountFromNumber).balance(150L).build();
        var accountTo = AccountEntity.builder().accountNumber(accountToNumber).balance(0L).build();
        accountRepository.saveAll(List.of(accountFrom, accountTo));
        //when
        financialMovement.transfer(
                FinancialMovementRequest.builder()
                        .operationId(UUID.randomUUID())
                        .amount(amount)
                        .accountFrom(accountFromNumber)
                        .accountTo(accountToNumber)
                        .build());
        //then
        assertEquals(0, accountRepository.findByAccountNumber(accountFromNumber).getBalance());
        assertEquals(150L, accountRepository.findByAccountNumber(accountToNumber).getBalance());
    }

    @Test
    void should_throw_when_insuficient_balance_from(){
        //given
        Long amount = 150L;
        int accountFromNumber = 443520;
        int accountToNumber = 302210;
        var accountFrom = AccountEntity.builder().accountNumber(accountFromNumber).balance(100L).build();
        var accountTo = AccountEntity.builder().accountNumber(accountToNumber).balance(0L).build();
        accountRepository.saveAll(List.of(accountFrom, accountTo));

        FinancialMovementRequest financialMovementBuild = FinancialMovementRequest.builder()
                .operationId(UUID.randomUUID())
                .amount(amount)
                .accountFrom(accountFromNumber)
                .accountTo(accountToNumber)
                .build();

        //when
        assertThrows(IllegalStateException.class, () -> {

            financialMovement.transfer(financialMovementBuild);
        });
        //then
        assertEquals(100L, accountRepository.findByAccountNumber(accountFromNumber).getBalance());
        assertEquals(0L, accountRepository.findByAccountNumber(accountToNumber).getBalance());
    }


}
