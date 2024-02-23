package com.example.hotelnebula;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Model.User;
import Model.UserAdapter;

public class UserListViewActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView lvUsers;
    ArrayList<User> userList;
    UserAdapter userAdapter;
    Button btnAdd;

    DatabaseReference usersDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list_view);
        initialize();
    }

    private void initialize() {
        lvUsers = findViewById(R.id.lvUsers);
        usersDatabase = FirebaseDatabase.getInstance().getReference("users");
        btnAdd = findViewById(R.id.btnAdd);

        userList = new ArrayList<>();

        usersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList = new ArrayList<>();
                for (DataSnapshot datas : snapshot.getChildren())
                {
                    String username = datas.getKey();
                    String email=datas.child("email").getValue().toString();
                    String name=datas.child("name").getValue().toString();
                    String password=datas.child("password").getValue().toString();
                    String role=datas.child("role").getValue().toString();

                    User user = new User(username,email, name,password,role);
                    userList.add(user);
                }
                loadAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserListViewActivity.this, UserInfoActivity.class);
                startActivity(intent);
            }
        });
    }

    public void loadAdapter() {
        userAdapter = new UserAdapter(this, userList);
        lvUsers.setAdapter(userAdapter);
        lvUsers.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, UserInfoActivity.class);
        intent.putExtra("userToUpdate", userList.get(position));
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadAdapter();
    }
}