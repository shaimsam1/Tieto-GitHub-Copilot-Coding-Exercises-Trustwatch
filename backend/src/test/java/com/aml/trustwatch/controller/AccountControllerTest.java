package com.aml.trustwatch.controller;

import com.aml.trustwatch.model.Account;
import com.aml.trustwatch.model.RuleMatch;
import com.aml.trustwatch.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Test
    void shouldReturnAllAccounts() throws Exception {
        Account account = createTestAccount("ACC-001");
        when(accountService.getAllAccounts()).thenReturn(List.of(account));

        mockMvc.perform(get("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").isArray())
            .andExpect(jsonPath("$.data[0].accountId").value("ACC-001"));
    }

    @Test
    void shouldReturnAccountById() throws Exception {
        Account account = createTestAccount("ACC-001");
        when(accountService.getAccountById("ACC-001")).thenReturn(account);

        mockMvc.perform(get("/api/accounts/ACC-001")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.accountId").value("ACC-001"));
    }

    @Test
    void shouldReturnHighRiskAccounts() throws Exception {
        Account account = createTestAccount("ACC-001");
        when(accountService.getHighRiskAccounts()).thenReturn(List.of(account));

        mockMvc.perform(get("/api/accounts/high-risk")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void shouldAnalyzeAccount() throws Exception {
        when(accountService.analyzeAccount("ACC-001")).thenReturn(List.of());

        mockMvc.perform(get("/api/accounts/ACC-001/analyze")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data").isArray());
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

