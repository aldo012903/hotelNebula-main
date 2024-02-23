package com.example.hotelnebula;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements  View.OnClickListener{

    Button btnProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();

    }

    private void initialize() {
        btnProfile = findViewById(R.id.btnProgile);
        btnProfile.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id==R.id.btnProgile) goToProfile();
    }

    private void goToProfile() {
        Intent intent =new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }


}