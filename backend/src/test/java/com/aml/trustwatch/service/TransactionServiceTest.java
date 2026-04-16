package com.aml.trustwatch.service;

import com.aml.trustwatch.exception.ResourceNotFoundException;
import com.aml.trustwatch.loader.TransactionLoader;
import com.aml.trustwatch.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionLoader transactionLoader;

    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        transactionService = new TransactionService(transactionLoader);
    }

    @Test
    void shouldReturnAllTransactions() {
        List<Transaction> expectedTransactions = List.of(createTestTransaction("TXN-001"));
        when(transactionLoader.getAllTransactions()).thenReturn(expectedTransactions);

        List<Transaction> result = transactionService.getAllTransactions();

        assertThat(result).isEqualTo(expectedTransactions);
    }

    @Test
    void shouldReturnTransactionById() {
        Transaction expectedTransaction = createTestTransaction("TXN-001");
        when(transactionLoader.getTransactionById("TXN-001")).thenReturn(Optional.of(expectedTransaction));

        Transaction result = transactionService.getTransactionById("TXN-001");

        assertThat(result).isEqualTo(expectedTransaction);
    }

    @Test
    void shouldThrowExceptionWhenTransactionNotFound() {
        when(transactionLoader.getTransactionById("TXN-999")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transactionService.getTransactionById("TXN-999"))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("TXN-999");
    }

    @Test
    void shouldReturnTransactionsForAccount() {
        List<Transaction> expectedTransactions = List.of(createTestTransaction("TXN-001"));
        when(transactionLoader.getTransactionsForAccount("ACC-001")).thenReturn(expectedTransactions);

        List<Transaction> result = transactionService.getTransactionsForAccount("ACC-001");

        assertThat(result).isEqualTo(expectedTransactions);
    }

    @Test
    void shouldReturnHighRiskTransactions() {
        List<Transaction> expectedTransactions = List.of(createTestTransaction("TXN-001"));
        when(transactionLoader.getHighRiskTransactions(70)).thenReturn(expectedTransactions);

        List<Transaction> result = transactionService.getHighRiskTransactions(70);

        assertThat(result).isEqualTo(expectedTransactions);
    }

    private Transaction createTestTransaction(String id) {
        Transaction transaction = new Transaction();
        transaction.setTransactionId(id);
        transaction.setAccountId("ACC-001");
        transaction.setCustomerName("Test Customer");
        transaction.setAmount(new BigDecimal("9500.00"));
        transaction.setCurrency("EUR");
        transaction.setMerchantName("Test Merchant");
        transaction.setMerchantCategory("CRYPTO_EXCHANGE");
        transaction.setOriginCountry("SE");
        transaction.setDestinationCountry("MT");
        transaction.setRiskScore(85);
        transaction.setFraudIndicators(List.of("VELOCITY_ANOMALY"));
        transaction.setTimestamp(Instant.now());
        transaction.setStatus("PENDING_REVIEW");
        return transaction;
    }
}

