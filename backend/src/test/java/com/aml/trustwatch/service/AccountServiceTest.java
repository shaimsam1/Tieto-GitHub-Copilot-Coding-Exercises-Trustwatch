package com.aml.trustwatch.service;

import com.aml.trustwatch.exception.ResourceNotFoundException;
import com.aml.trustwatch.loader.AccountLoader;
import com.aml.trustwatch.model.Account;
import com.aml.trustwatch.model.AmlFlag;
import com.aml.trustwatch.model.RuleMatch;
import com.aml.trustwatch.rules.RuleEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountLoader accountLoader;
    
    @Mock
    private RuleEngine ruleEngine;

    private AccountService accountService;

    @BeforeEach
    void setUp() {
        accountService = new AccountService(accountLoader, ruleEngine);
    }

    @Test
    void shouldReturnAllAccounts() {
        List<Account> expectedAccounts = List.of(createTestAccount("ACC-001"));
        when(accountLoader.getAllAccounts()).thenReturn(expectedAccounts);

        List<Account> result = accountService.getAllAccounts();

        assertThat(result).isEqualTo(expectedAccounts);
    }

    @Test
    void shouldReturnAccountById() {
        Account expectedAccount = createTestAccount("ACC-001");
        when(accountLoader.getAccountById("ACC-001")).thenReturn(Optional.of(expectedAccount));

        Account result = accountService.getAccountById("ACC-001");

        assertThat(result).isEqualTo(expectedAccount);
    }

    @Test
    void shouldThrowExceptionWhenAccountNotFound() {
        when(accountLoader.getAccountById("ACC-999")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.getAccountById("ACC-999"))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("ACC-999");
    }

    @Test
    void shouldReturnAccountsByRiskRating() {
        List<Account> expectedAccounts = List.of(createTestAccount("ACC-001"));
        when(accountLoader.getAccountsByRiskRating("HIGH")).thenReturn(expectedAccounts);

        List<Account> result = accountService.getAccountsByRiskRating("HIGH");

        assertThat(result).isEqualTo(expectedAccounts);
    }

    @Test
    void shouldAnalyzeAccount() {
        Account account = createTestAccount("ACC-001");
        List<RuleMatch> expectedMatches = List.of();
        when(accountLoader.getAccountById("ACC-001")).thenReturn(Optional.of(account));
        when(ruleEngine.evaluate(account)).thenReturn(expectedMatches);

        List<RuleMatch> result = accountService.analyzeAccount("ACC-001");

        assertThat(result).isEqualTo(expectedMatches);
    }

    private Account createTestAccount(String id) {
        Account account = new Account();
        account.setAccountId(id);
        account.setCustomerName("Test Customer");
        account.setIban("DE89370400440532013000");
        account.setAccountType("BUSINESS");
        account.setOpenedDate(LocalDate.of(2021, 6, 1));
        account.setRiskRating("HIGH");
        account.setAmlFlags(List.of());
        return account;
    }
}

