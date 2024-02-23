package com.example.hotelnebula;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import android.util.Log;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity implements  View.OnClickListener{

    EditText loginUsername, loginPass;
    Button btnLogin,btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialize();
    }

    private void initialize(){

        // Text Fields
        loginUsername = findViewById(R.id.usernamelogin);
        loginPass = findViewById(R.id.passwordlogin);
        // Buttons
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        // Onclick
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        if (id == R.id.btnLogin)
            checkUser();
        if (id == R.id.btnRegister)
            goToRegister();
    }

    private void goToRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void checkFields(){
        String username = loginUsername.getText().toString().trim();
        String password = loginPass.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill up the information", Toast.LENGTH_SHORT).show();
            Log.d("LoginActivity", "Empty fields detected");
        }
    }

    public void checkUser() {
        checkFields();
        String usernameLogin = loginUsername.getText().toString().trim();
        String passwordLogin = loginPass.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkuserdata = reference.child(usernameLogin);

        checkuserdata.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                        //Long passwordLong = userSnapshot.child("password").getValue(Long.class);
                        //String passFromDB = String.valueOf(passwordLong);
                        String passFromDB = snapshot.child("password").getValue(String.class);

                        if (passFromDB.equals(passwordLogin)) {
                            String namedb = snapshot.child("name").getValue(String.class);
                            String userdb = snapshot.getKey();
                            saveUserData(namedb, passwordLogin, snapshot.child("email").getValue().toString(), snapshot.getKey().toString(),snapshot.child("role").getValue().toString());

                            Intent intent = new Intent(LoginActivity.this, SearchActivity.class);
                            SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("username", usernameLogin);
                            editor.apply();

                            Toast.makeText(LoginActivity.this, "Login success", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                            finish();
                            return;
                        } else {
                            Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                            Log.d("LoginActivity", "Invalid email or password");
                        }
                } else {
                    Toast.makeText(LoginActivity.this, "The person does not exist", Toast.LENGTH_SHORT).show();
                    Log.d("LoginActivity", "User does not exist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("LoginActivity", "Database Error: " + error.getMessage());
            }
        });
    }

    private void saveUserData(String name, String password, String email, String username, String role) {
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("name", name);
        editor.putString("password", password);
        editor.putString("email", email);
        editor.putString("username", username);
        editor.putString("role", role);
        editor.apply();
    }


}