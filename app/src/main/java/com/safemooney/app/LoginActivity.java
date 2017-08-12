package com.safemooney.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.safemooney.http.models.*;
import com.safemooney.http.*;
import com.safemooney.R;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

public class LoginActivity extends AppCompatActivity
{


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        Button signinBtn = (Button) findViewById(R.id.signin_btn);
        signinBtn.setOnClickListener(new ButtonClickListener(this));

    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences preferences = getSharedPreferences("userdata", MODE_PRIVATE);
        int userId = preferences.getInt("userId", -1);

        if(userId < 0)
            return;

        Intent mainIntent = new Intent();
        mainIntent.setClass(this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }

    private class ButtonClickListener implements View.OnClickListener
    {
        private Context context;

        public ButtonClickListener(Context context)
        {
            this.context = context;
        }

        @Override
        public void onClick(View view)
        {
            try
            {
                final AccountClient accountClient = new AccountClient();

                final User[] currentUser = {null};

                final String username = ((EditText) findViewById(R.id.loin_edit)).getText().toString();
                final String password = ((EditText) findViewById(R.id.password_edit)).getText().toString();

                Thread accountThread = new Thread(new Runnable() {
                    @Override
                    public void run()
                    {
                        currentUser[0] = accountClient.logIn(username, password);
                    }
                });
                accountThread.start();
                accountThread.join();

                if(currentUser[0] == null)
                {
                    Toast toast = Toast.makeText(context, "This user is not found",Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }

                SharedPreferences.Editor preferencesEditor = context.getSharedPreferences("userdata", MODE_PRIVATE).edit();
                preferencesEditor.putInt("userId", currentUser[0].getId());
                preferencesEditor.putString("username", currentUser[0].getUsername());
                preferencesEditor.putString("firstname", currentUser[0].getFirstname());
                preferencesEditor.putString("lastname", currentUser[0].getLastname());
                preferencesEditor.putString("tokenkey", currentUser[0].getTokenkey());
                preferencesEditor.apply();

                Intent mainIntent = new Intent();
                mainIntent.setClass(context, MainActivity.class);
                context.startActivity(mainIntent);
                finish();
            }
            catch (Exception e)
            {
                Log.d("mytag", e.toString());
            }
        }
    }
}
