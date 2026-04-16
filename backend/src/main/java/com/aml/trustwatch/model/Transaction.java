package com.aml.trustwatch.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class Transaction {

    private String transactionId;
    private String accountId;
    private String customerName;
    private BigDecimal amount;
    private String currency;
    private String merchantName;
    private String merchantCategory;
    private String originCountry;
    private String destinationCountry;
    private int riskScore;
    private List<String> fraudIndicators;
    private Instant timestamp;
    private String status;

    public Transaction() {
    }

    public Transaction(String transactionId, String accountId, String customerName,
                       BigDecimal amount, String currency, String merchantName,
                       String merchantCategory, String originCountry, String destinationCountry,
                       int riskScore, List<String> fraudIndicators, Instant timestamp, String status) {
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.customerName = customerName;
        this.amount = amount;
        this.currency = currency;
        this.merchantName = merchantName;
        this.merchantCategory = merchantCategory;
        this.originCountry = originCountry;
        this.destinationCountry = destinationCountry;
        this.riskScore = riskScore;
        this.fraudIndicators = fraudIndicators;
        this.timestamp = timestamp;
        this.status = status;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantCategory() {
        return merchantCategory;
    }

    public void setMerchantCategory(String merchantCategory) {
        this.merchantCategory = merchantCategory;
    }

    public String getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(String originCountry) {
        this.originCountry = originCountry;
    }

    public String getDestinationCountry() {
        return destinationCountry;
    }

    public void setDestinationCountry(String destinationCountry) {
        this.destinationCountry = destinationCountry;
    }

    public int getRiskScore() {
        return riskScore;
    }

    public void setRiskScore(int riskScore) {
        this.riskScore = riskScore;
    }

    public List<String> getFraudIndicators() {
        return fraudIndicators;
    }

    public void setFraudIndicators(List<String> fraudIndicators) {
        this.fraudIndicators = fraudIndicators;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

