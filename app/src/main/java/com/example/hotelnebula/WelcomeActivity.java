package com.example.hotelnebula;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class WelcomeActivity extends AppCompatActivity implements  View.OnClickListener{

    Button btnLogin, btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initialize();
    }

    private void initialize() {
        btnLogin = findViewById(R.id.btnLogin);
        btnSignup = findViewById(R.id.btnSignup);
        btnLogin.setOnClickListener(this);
        btnSignup.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id==R.id.btnLogin) goToLogin();
        if (id==R.id.btnSignup) goToSignup();
    }

    private void goToSignup() {
        Intent intent =new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void goToLogin() {
        Intent intent =new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}