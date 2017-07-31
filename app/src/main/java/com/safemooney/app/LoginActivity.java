package com.safemooney.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.safemooney.R;

public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        TextView loginView = (TextView) findViewById(R.id.loin_edit);
        TextView passwordView = (TextView) findViewById(R.id.password_edit);
        Button signinBtn = (Button) findViewById(R.id.signin_btn);

        final Intent mainIntent = new Intent();
        mainIntent.setClass(this, MainActivity.class);

        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                    startActivity(mainIntent);
                    finish();
                }
                catch (Exception e){
                    Log.d("mytag", "", e);
                }
            }
        });

    }
}
