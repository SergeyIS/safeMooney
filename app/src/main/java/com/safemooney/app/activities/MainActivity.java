package com.safemooney.app.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.safemooney.R;
import com.safemooney.app.adapters.TransactionAdapter;
import com.safemooney.http.AccountClient;
import com.safemooney.http.TransactionClient;
import com.safemooney.http.models.TransactionPreview;
import com.safemooney.http.models.User;
import com.safemooney.http.models.Transaction;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{


    private final static List<TransactionPreview> TRANSACTION_LIST = new ArrayList<>();
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //InitializeTransactionList();

        ListView transactionsView = (ListView) findViewById(R.id.transactions_view);
        TransactionAdapter adapter = new TransactionAdapter(this, R.layout.transaction_item, TRANSACTION_LIST);
        transactionsView.setAdapter(adapter);


        //getting currentUser
        SharedPreferences preferences = getSharedPreferences("userdata", MODE_PRIVATE);
        int userId = preferences.getInt("userId", -1);
        String username = preferences.getString("username", null);
        String firstname = preferences.getString("firstname", null);
        String lastname = preferences.getString("lastname", null);
        String tokenkey = preferences.getString("tokenkey", null);

        if(userId < 0 || username == null || firstname == null || lastname == null || tokenkey == null)
        {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }
        else
        {
            currentUser = new User();
            currentUser.setId(userId);
            currentUser.setUsername(username);
            currentUser.setFirstname(firstname);
            currentUser.setLastname(lastname);
            currentUser.setTokenkey(tokenkey);
        }
        //

    }

    @Override
    protected void onResume(){
        super.onResume();

        AsyncTask<Void, Integer, Void> asyncTask = new AsyncTask<Void, Integer, Void>() {

            List<TransactionPreview> transactionList;
            @Override
            protected Void doInBackground(Void... unused)
            {
                try
                {
                    TransactionClient transactionClient = new TransactionClient(currentUser.getId(), currentUser.getTokenkey());
                    transactionList = transactionClient.fetchTransactions();
                    if(transactionList == null)
                    {

                    }
                }
                catch(Exception e)
                {
                    Log.d("mytag", e.toString());
                }

                return(null);
            }

            @Override
            protected void onPostExecute(Void unused) {
                if (transactionList == null)
                    return;

                ListView transactionsView = (ListView) findViewById(R.id.transactions_view);
                ArrayAdapter<TransactionPreview> adapter = (ArrayAdapter<TransactionPreview>) transactionsView.getAdapter();
                adapter.clear();
                adapter.addAll(transactionList);
            }
        };

        asyncTask.execute();
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

            case (R.id.addnew_bar):{

                Intent findUserIntent = new Intent();
                findUserIntent.setClass(this, FindUserActivity.class);
                startActivity(findUserIntent);
                break;
            }
            case (R.id.logout_bar):
                logOut();
                break;
            case (R.id.notification_bar):
            {
                Intent noticeIntent = new Intent();
                noticeIntent.setClass(this, NoticeActivity.class);
                startActivity(noticeIntent);
                break;
            }
        }
        return true;
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
//        for(int i = 0; i < 10 ; i++)
//            TRANSACTION_LIST.add(new Transaction("Sergey Ivanovich" + i, "123.45", new byte[1], true));

    }



}
