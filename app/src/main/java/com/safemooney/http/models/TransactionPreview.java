package com.safemooney.http.models;


import java.security.cert.Extension;

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


    @Override
    public boolean equals(Object obj)
    {
        if(obj == null)
            return false;

        try
        {
            TransactionPreview local = (TransactionPreview) obj;

            return local.getTransactionData().getId() == this.transactionData.getId();

        }
        catch (Exception e)
        {
            return false;
        }
    }
}
