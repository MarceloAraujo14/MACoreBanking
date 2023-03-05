package com.maraujo.corebanking.core;

import com.maraujo.corebanking.core.repository.AccountRepository;
import com.maraujo.corebanking.core.request.TransferRequest;
import com.maraujo.corebanking.core.service.AccountService;
import com.maraujo.corebanking.core.service.FinancialMovementService;
import com.maraujo.corebanking.core.entity.AccountEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author maraujo
 * Classe de Teste Funcional da classe FinancialMovementService
 *
 */

@SpringBootTest
@ActiveProfiles("test")
class FinancialMovementServiceTest {

    @Autowired
    FinancialMovementService financialMovementService;
    @Autowired
    AccountService accountService;
    @Autowired
    AccountRepository accountRepository;

    @AfterEach
    void tearDown() {
        accountService.deleteAll();
    }

    /**
     * Testa a movimentação do saldo entre a conta de origem e a conta de destino
     */
    @Test
    void should_transfer_from_origin_to_destiny_account(){
        //given
        Long amount = 150L;
        int accountFromNumber = 443520;
        int accountToNumber = 302210;
        var accountFrom = AccountEntity.builder().accountNumber(accountFromNumber).balance(150L).build();
        var accountTo = AccountEntity.builder().accountNumber(accountToNumber).balance(0L).build();
        accountService.saveAll(List.of(accountFrom, accountTo));
        //when
        financialMovementService.transfer(
                TransferRequest.builder()
                        .operationId(UUID.randomUUID())
                        .amount(amount)
                        .accountFrom(accountFromNumber)
                        .accountTo(accountToNumber)
                        .build());
        //then
        assertEquals(0, accountService.findByAccountNumber(accountFromNumber).getBalance());
        assertEquals(150L, accountService.findByAccountNumber(accountToNumber).getBalance());
    }

    /**
     * Deve bloquear a movimentação de saldo da conta de origem com saldo menor que o necessário
     */
    @Test
    void should_throw_when_insuficient_balance_from(){
        //given
        Long amount = 150L;
        int accountFromNumber = 443520;
        int accountToNumber = 302210;
        var accountFrom = AccountEntity.builder().accountNumber(accountFromNumber).balance(100L).build();
        var accountTo = AccountEntity.builder().accountNumber(accountToNumber).balance(0L).build();
        accountService.saveAll(List.of(accountFrom, accountTo));

        TransferRequest transferRequestBuild = TransferRequest.builder()
                .operationId(UUID.randomUUID())
                .amount(amount)
                .accountFrom(accountFromNumber)
                .accountTo(accountToNumber)
                .build();

        //when
        assertThrows(IllegalStateException.class, () -> financialMovementService.transfer(transferRequestBuild));
        //then
        assertEquals(100L, accountService.findByAccountNumber(accountFromNumber).getBalance());
        assertEquals(0L, accountService.findByAccountNumber(accountToNumber).getBalance());
    }


}
