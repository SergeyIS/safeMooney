package com.safemooney.app.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.safemooney.R;
import com.safemooney.app.adapters.UserAdapter;
import com.safemooney.http.TransactionClient;
import com.safemooney.http.models.User;
import com.safemooney.http.models.UserPreview;

import java.util.ArrayList;
import java.util.List;

public class FindUserActivity extends AppCompatActivity
{
    private final static List<UserPreview> USER_LIST = new ArrayList<UserPreview>();
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_user);


        EditText searchEdit = (EditText)findViewById(R.id.search_edit);
        ListView userList = (ListView) findViewById(R.id.userlist);
        userList.setAdapter(new UserAdapter(this, R.layout.user_preview_item, USER_LIST));
        userList.setOnItemClickListener(new ItemOnClickListener(this));
        searchEdit.addTextChangedListener(new SearchTextWatcher());

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
    protected void onResume()
    {
        super.onResume();

        AsyncTask<Void, Integer, Void> asyncTask = new AsyncTask<Void, Integer, Void>() {

            List<UserPreview> userList;
            @Override
            protected Void doInBackground(Void... unused)
            {
                try
                {
                    TransactionClient transactionClient = new TransactionClient(currentUser.getId(), currentUser.getTokenkey());
                    userList = transactionClient.getUserList();
                }
                catch(Exception e)
                {
                    Log.d("mytag", e.toString());
                }

                return(null);
            }

            @Override
            protected void onPostExecute(Void unused)
            {
                if(userList == null)
                    return;

                ListView usersView = (ListView) findViewById(R.id.userlist);
                ArrayAdapter<UserPreview> adapter = (ArrayAdapter<UserPreview>) usersView.getAdapter();
                adapter.clear();
                adapter.addAll(userList);
            }
        };

        asyncTask.execute();
    }


    private class SearchTextWatcher implements TextWatcher
    {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void afterTextChanged(Editable editable)
        {
            Log.d("mytag", editable.toString());
        }
    }

    private class ItemOnClickListener implements AdapterView.OnItemClickListener
    {
        private Context context;

        public ItemOnClickListener(Context context)
        {
            this.context = context;
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
        {
            UserPreview preview = (UserPreview)adapterView.getItemAtPosition(i);
            if(preview == null)
                return;

            Intent addTransactionIntent = new Intent();
            addTransactionIntent.setClass(context, AddTransactionActivity.class);
            addTransactionIntent.putExtra("userId", preview.getUserId());
            addTransactionIntent.putExtra("username", preview.getUsername());
            addTransactionIntent.putExtra("firstName", preview.getFirstName());
            addTransactionIntent.putExtra("lastName", preview.getLastName());
            context.startActivity(addTransactionIntent);
            finish();
        }
    }
}
