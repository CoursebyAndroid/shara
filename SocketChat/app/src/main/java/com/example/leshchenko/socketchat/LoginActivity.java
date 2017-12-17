package com.example.leshchenko.socketchat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;


public class LoginActivity extends AppCompatActivity {
    private EditText edText;
    private AppCompatButton btnOk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edText = (EditText) findViewById(R.id.ed_login);
        btnOk = (AppCompatButton) findViewById(R.id.btn_login_ok);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                intent.putExtra("user",edText.getText().toString());
                startActivity(intent);
            }
        });

    }




}
