package com.safemooney.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.safemooney.R;

import java.util.ArrayList;

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case (R.id.addnew_bar):
                addNewTransaction();
                break;
        }
        return true;
    }

    private void addNewTransaction(){

        Intent addTransIntent = new Intent();
        addTransIntent.setClass(this, AddTransactionActivity.class);

        try{
            startActivity(addTransIntent);
        }
        catch (Exception e){

        }

    }
    private void InitializeTransactionList(){

        transactionList = new ArrayList<Transaction>();
        for(int i = 0; i < 10 ; i++)
            transactionList.add(new Transaction("Sergey Ivanovich" + i, "123.45", new byte[1], true));


    }

}
