package com.safemooney.app.activities;

import com.safemooney.R;
import com.safemooney.http.TransactionClient;
import com.safemooney.http.models.Transaction;
import com.safemooney.http.models.UserPreview;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.Date;


public class AddTransactionActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtransaction);

        Intent findUserIntent = getIntent();
        UserPreview preview = new UserPreview();

        preview.setUserId(findUserIntent.getIntExtra("userId", -1));
        preview.setUsername(findUserIntent.getStringExtra("username"));
        preview.setFirstName(findUserIntent.getStringExtra("firstName"));
        preview.setLastName(findUserIntent.getStringExtra("lastName"));

        Spinner currency = (Spinner) findViewById(R.id.currency_spn);
        currency.setAdapter(new CurrencyAdapter(this));

        TextView usernameText = (TextView) findViewById(R.id.username_text);
        TextView firstnameText = (TextView) findViewById(R.id.largename_text);
        firstnameText.setText(preview.getFirstName() + " " + preview.getLastName());
        usernameText.setText("@" + preview.getUsername());

        Button okBtn = (Button) findViewById(R.id.ok_btn);
        Button cnclByn = (Button) findViewById(R.id.cncl_btn);
        okBtn.setOnClickListener(new ButtonClickListener(preview.getUserId()));
        cnclByn.setOnClickListener(new ButtonClickListener(preview.getUserId()));

        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new PeriodChangedListener());

    }





    private class PeriodChangedListener implements SeekBar.OnSeekBarChangeListener
    {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b)
        {
            TextView daysView = (TextView) findViewById(R.id.daysView);
            daysView.setText(Integer.toString(i) + "Days");
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }


    private class ButtonClickListener implements View.OnClickListener
    {
        private int user2Id;

        public ButtonClickListener(int userId)
        {
            this.user2Id = userId;
        }

        @Override
        public void onClick(View view)
        {
            Button btn = (Button) view;

            if(btn.getId() == R.id.cncl_btn)
            {
                finish();
                return;
            }

            EditText countText = (EditText) findViewById(R.id.count_text);
            Spinner currency = (Spinner) findViewById(R.id.currency_spn);
            SeekBar daysView = (SeekBar) findViewById(R.id.seekBar);

            final String count = countText.getText().toString() + currency.getSelectedItem().toString();
            ;
            final int period = daysView.getProgress();

            Thread addTransactionThread = new Thread(new Runnable() {
                @Override
                public void run()
                {
                    try
                    {
                        //getting currentUser
                        SharedPreferences preferences = getSharedPreferences("userdata", MODE_PRIVATE);
                        int user1Id = preferences.getInt("userId", -1);
                        String tokenkey = preferences.getString("tokenkey", null);

                        if(user1Id < 0 || tokenkey == null)
                            return;

                        Transaction trans = new Transaction();
                        trans.setUser1Id(user1Id);
                        trans.setUser2Id(user2Id);
                        trans.setCount(count);
                        trans.setPeriod(period);
                        trans.setDate(new Date());
                        trans.setClosed(false);
                        trans.setPermited(false);

                        TransactionClient transactionClient = new TransactionClient(user1Id, tokenkey);
                        boolean result = transactionClient.addTransaction(trans);
                        if(!result)
                            Log.d("mytag", "data's not send");

                    }
                    catch (Exception e)
                    {
                        Log.d("mytag", e.toString());
                    }
                }
            });
            addTransactionThread.start();

            finish();

        }
    }


    private class CurrencyAdapter implements SpinnerAdapter
    {
        private final String[] currencyList = new String[] { "p.", "$", "€" };
        private Context context;

        public CurrencyAdapter(Context context)
        {
            this.context = context;
        }

        @Override
        public View getDropDownView(int i, View view, ViewGroup viewGroup)
        {
            TextView elementView = new TextView(context);
            elementView.setText(currencyList[i]);
            elementView.setTextSize(18);
            return elementView;
        }

        @Override
        public void registerDataSetObserver(DataSetObserver dataSetObserver) {

        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

        }

        @Override
        public int getCount() {
            return currencyList.length;
        }

        @Override
        public Object getItem(int i) {
            return currencyList[i];
        }

        @Override
        public long getItemId(int i) {
            return Long.valueOf(i);
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup)
        {
            TextView elementView = new TextView(context);
            elementView.setText(currencyList[i]);
            return elementView;
        }

        @Override
        public int getItemViewType(int i) {
            return i;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public boolean isEmpty() {
            return currencyList.length == 0;
        }
    }

}