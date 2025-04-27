package com.example.stepuptest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Login extends AppCompatActivity {
    EditText editTextUsername, editTextPassword;
    Button buttonLogin;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ImageView imageView = findViewById(R.id.imageViewIcon);
        imageView.setImageResource(R.drawable.ic_launcher_foreground);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String username = editTextUsername.getText().toString();
               String password = editTextPassword.getText().toString();

               if (username.equals("admin") && password.equals("1234")) {
                   Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                   // Intent to open another page
                   // startActivity(new Intent(LoginActivity.this, HomeActivity.class));

                   startActivity(new Intent(Login.this, Home.class));

               } else {
                   Toast.makeText(Login.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
               }
           }
       }
        );

        bottomNavigationView=findViewById(R.id.nav_view);
        bottomNavigationView.setSelectedItemId(R.id.login);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.navigation_home) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.power_up) {
                startActivity(new Intent(getApplicationContext(), PowerUp.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.login) {
                return true;
            }
            else if (id == R.id.rank) {
                startActivity(new Intent(getApplicationContext(), Rank.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });

    }
    @Override
    protected void onResume(){
        super.onResume();
        bottomNavigationView.setSelectedItemId(R.id.login);
    }
}