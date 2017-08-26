package com.safemooney.app.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.safemooney.R;
import com.safemooney.app.adapters.NoticeAdapter;
import com.safemooney.app.services.NoticeService;
import com.safemooney.http.AccountClient;
import com.safemooney.http.TransactionClient;
import com.safemooney.http.models.TransactionPreview;
import com.safemooney.http.models.User;
import com.safemooney.http.models.UserPreview;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class NoticeActivity extends AppCompatActivity
{
    private List<TransactionPreview> transactionPreviewList = new ArrayList<>();
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

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

        ListView noticeView = (ListView) findViewById(R.id.notice_view);
        noticeView.setAdapter(new NoticeAdapter(this,R.layout.notice_item ,transactionPreviewList, currentUser));

    }

    @Override
    protected void onResume()
    {
        super.onResume();

        ListView noticeView = (ListView) findViewById(R.id.notice_view);
        NoticeAdapter adapter =  (NoticeAdapter) noticeView.getAdapter();
        Queue<TransactionPreview> previews = NoticeService.getQueue();
        if(previews == null)
            return;
        adapter.addAll(previews);
        updateImages(currentUser.getId(), currentUser.getTokenkey());
    }

    private void updateImages(final int userId, final String tokenKey)
    {
        ListView listView = (ListView) findViewById(R.id.notice_view);
        int count = listView.getCount();
        final int[] counter = new int[1];
        while (counter[0] < count)
        {
            final Bitmap[] bitmaps = new Bitmap[1];
            final int positionOfItem = counter[0];
            final TransactionPreview tp = ((ArrayAdapter<TransactionPreview>) listView.getAdapter()).getItem(positionOfItem);

            AsyncTask<Void, Integer, Void> asyncTask = new AsyncTask<Void, Integer, Void>()
            {
                @Override
                protected Void doInBackground(Void... voids)
                {
                    try
                    {
                        AccountClient accountClient = new AccountClient();
                        Bitmap buf = accountClient.getImage(tp.getUserData().getUserId());
                        bitmaps[0] = buf;
                    }
                    catch (Exception e)
                    {
                        Log.d("mytag", e.toString());
                    }

                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid)
                {
                    ListView listView = (ListView) findViewById(R.id.notice_view);
                    ArrayAdapter<TransactionPreview> adapter = (ArrayAdapter<TransactionPreview>) listView.getAdapter();

                    TransactionPreview preview = adapter.getItem(positionOfItem);
                    preview.getUserData().setBitmap(bitmaps[0]);
                    adapter.notifyDataSetChanged();
                }
            };
            asyncTask.execute();

            counter[0]++;
        }

    }

    private void acceptTransaction(final int transId, final int userId, final String tokenKey)
    {
        Thread th = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                TransactionClient transactionClient = new TransactionClient(userId, tokenKey);
                transactionClient.confirmTransaction(transId);
            }
        });
        th.start();
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
            finish();
        }
    }
}
