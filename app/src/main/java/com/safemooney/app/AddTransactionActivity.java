package com.safemooney.app;

import com.safemooney.R;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class AddTransactionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtransaction);

        Button ok_btn = (Button) findViewById(R.id.ok_btn);
        Button cancel_btn = (Button) findViewById(R.id.cancel_btn);
        OnClickHandler handler = new OnClickHandler();

        ok_btn.setOnClickListener(handler);
        cancel_btn.setOnClickListener(handler);
    }


    private class OnClickHandler implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            finish();
        }
    }
}