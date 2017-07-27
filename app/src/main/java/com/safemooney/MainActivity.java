package com.safemooney;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MainActivity extends AppCompatActivity {

    ArrayList<Transaction> transactionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitializeTransactionList();

        TransactionAdapter transactionAdapter = new TransactionAdapter(this, R.layout.transaction_item, transactionList);

        ListView transactionsView = (ListView) findViewById(R.id.transactions_view);

        transactionsView.setAdapter(transactionAdapter);

    }


    private void InitializeTransactionList(){

        transactionList = new ArrayList<Transaction>();
        for(int i = 0; i < 10 ; i++)
            transactionList.add(new Transaction("Sergey Ivanovich" + i, "123.45", new byte[1], true));


    }

}
