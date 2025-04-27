package com.example.stepuptest;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PowerUp extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //ImageView imageView = findViewById(R.id.imageViewIcon_power);
        //imageView.setImageResource(R.drawable.ic_main);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_power_up);
//        setContentView(R.layout.fragment_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // Button1: Pause Button
        Button button1 = findViewById(R.id.button1);
        button1.setOnClickListener(v -> {
            // Create Intent to start SecondActivity
//            Intent intent = new Intent(MainActivity.this, PowerUp.class);
//            // Start the activity
//            startActivity(intent);

            Intent intent = new Intent(PowerUp.this, Login.class);
            startActivity(intent);
        });

        // Button2: Koala Button
        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(v -> {
            // Create Intent to start SecondActivity
//            Intent intent = new Intent(MainActivity.this, PowerUp.class);
//            // Start the activity
//            startActivity(intent);
        });

        // Button3: x1.5
        Button button3 = findViewById(R.id.button3);
        button3.setOnClickListener(v -> {
            // Create Intent to start SecondActivity
//            Intent intent = new Intent(MainActivity.this, PowerUp.class);
//            // Start the activity
//            startActivity(intent);
        });

        // Button4: x3
        Button button4 = findViewById(R.id.button4);
        button4.setOnClickListener(v -> {
            // Create Intent to start SecondActivity
//            Intent intent = new Intent(MainActivity.this, PowerUp.class);
//            // Start the activity
//            startActivity(intent);
        });


        bottomNavigationView=findViewById(R.id.nav_view);
        bottomNavigationView.setSelectedItemId(R.id.power_up);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.navigation_home) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.power_up) {
                return true;

            } else if (id == R.id.login) {
                startActivity(new Intent(getApplicationContext(), Login.class));
                overridePendingTransition(0, 0);
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
        bottomNavigationView.setSelectedItemId(R.id.power_up);
    }
}