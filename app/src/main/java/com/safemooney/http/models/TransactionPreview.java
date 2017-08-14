package com.safemooney.http.models;


public class TransactionPreview
{
    private Transaction transactionData;
    private UserPreview userData;

    public TransactionPreview(Transaction t, UserPreview u)
    {
        this.transactionData = t;
        this.userData = u;
    }

    public Transaction getTransactionData() {
        return transactionData;
    }

    public void setTransactionData(Transaction transactionData) {
        this.transactionData = transactionData;
    }

    public UserPreview getUserData() {
        return userData;
    }

    public void setUserData(UserPreview userData) {
        this.userData = userData;
    }
}
