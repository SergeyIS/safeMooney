package com.safemooney.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.safemooney.R;
import com.safemooney.http.AccountClient;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{

    ArrayList<Transaction> transactionList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitializeTransactionList();

        TransactionAdapter transactionAdapter = new TransactionAdapter(this, R.layout.transaction_item, transactionList);

        ListView transactionsView = (ListView) findViewById(R.id.transactions_view);

        transactionsView.setAdapter(transactionAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        switch (item.getItemId()){

            case (R.id.addnew_bar):
                addNewTransaction();
                break;
            case (R.id.logout_bar):
                logOut();
                break;
        }
        return true;
    }

    private void addNewTransaction()
    {

        Intent addTransIntent = new Intent();
        addTransIntent.setClass(this, AddTransactionActivity.class);

        try
        {
            startActivity(addTransIntent);
        }
        catch (Exception e)
        {

        }

    }
    private void logOut()
    {
        SharedPreferences preferences = getSharedPreferences("userdata", MODE_PRIVATE);

        final int userId = preferences.getInt("userId", -1);
        final String tokenkey = preferences.getString("tokenkey", null);

        Thread logoutThread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                AccountClient accountClient = new AccountClient();
                try
                {
                    boolean result = accountClient.logOut(userId, tokenkey);

                    Log.d("mytag", "result of logout " + Boolean.toString(result));
                }
                catch(Exception e)
                {
                    Log.d("mytag", e.toString());
                }

            }
        });
        logoutThread.start();
        preferences.edit().putInt("userId", -1).apply();

        Intent loginIntent = new Intent();
        loginIntent.setClass(this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }
    private void InitializeTransactionList()
    {

        transactionList = new ArrayList<Transaction>();
        for(int i = 0; i < 10 ; i++)
            transactionList.add(new Transaction("Sergey Ivanovich" + i, "123.45", new byte[1], true));


    }

}
