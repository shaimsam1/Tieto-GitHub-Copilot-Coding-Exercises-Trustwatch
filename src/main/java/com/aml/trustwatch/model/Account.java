package com.aml.trustwatch.model;

import java.time.LocalDate;
import java.util.List;

public class Account {

    private String accountId;
    private String customerName;
    private String iban;
    private String accountType;
    private LocalDate openedDate;
    private String riskRating;
    private List<AmlFlag> amlFlags;

    public Account() {
    }

    public Account(String accountId, String customerName, String iban, String accountType,
                   LocalDate openedDate, String riskRating, List<AmlFlag> amlFlags) {
        this.accountId = accountId;
        this.customerName = customerName;
        this.iban = iban;
        this.accountType = accountType;
        this.openedDate = openedDate;
        this.riskRating = riskRating;
        this.amlFlags = amlFlags;
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

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public LocalDate getOpenedDate() {
        return openedDate;
    }

    public void setOpenedDate(LocalDate openedDate) {
        this.openedDate = openedDate;
    }

    public String getRiskRating() {
        return riskRating;
    }

    public void setRiskRating(String riskRating) {
        this.riskRating = riskRating;
    }

    public List<AmlFlag> getAmlFlags() {
        return amlFlags;
    }

    public void setAmlFlags(List<AmlFlag> amlFlags) {
        this.amlFlags = amlFlags;
    }
}

