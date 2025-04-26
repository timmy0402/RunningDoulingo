package com.example.stepuptest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDate;

public class PowerUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            long currency = getCurrency();
            if (currency >= 50 && !isPauseStreak()) {
                currency -= 50;
                SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putLong("currency", currency);
                editor.putBoolean("pauseStreak", true);
                editor.apply();
                getCurrency();
            }
            // Create Intent to start SecondActivity
//            Intent intent = new Intent(MainActivity.this, PowerUp.class);
//            // Start the activity
//            startActivity(intent)
//            Intent intent = new Intent(PowerUp.this, Login.class);
//            startActivity(intent);
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
            long currency = getCurrency();
            if (currency >= 10 && (int)getMultiplier() == 1) {
                currency -= 10;
                SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putLong("currency", currency);
                editor.putFloat("multiplier", 1.5f);
                editor.apply();
                getCurrency();
            }
            // Create Intent to start SecondActivity
//            Intent intent = new Intent(MainActivity.this, PowerUp.class);
//            // Start the activity
//            startActivity(intent);
        });

        // Button4: x3
        Button button4 = findViewById(R.id.button4);
        button4.setOnClickListener(v -> {
            long currency = getCurrency();
            if (currency >= 25 && getStreakMultiplier() == 1) {
                currency -= 25;
                SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putLong("currency", currency);
                editor.putInt("streakMultiplier", 3);
                editor.apply();
                getCurrency();
            }
            // Create Intent to start SecondActivity
//            Intent intent = new Intent(MainActivity.this, PowerUp.class);
//            // Start the activity
//            startActivity(intent);
        });

        BottomNavigationView bottomNavigationView=findViewById(R.id.nav_view);
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

    // Handle resume event
    @Override
    protected void onResume() {
        super.onResume();
        getCurrency();
    }

    private long getCurrency() {
        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        long currency = sharedPreferences.getLong("currency", 0);
        Log.d("Step Counter", "Loaded currency: " + currency);
        TextView curr = findViewById(R.id.shop_currency);
        curr.setText(String.valueOf(currency));
        return currency;
    }

    private boolean isPauseStreak() {
        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        boolean pauseStreak = sharedPreferences.getBoolean("pauseStreak", false);
        Log.d("Step Counter", "Loaded pause streak: " + pauseStreak);
        return pauseStreak;
    }

    private float getMultiplier() {
        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        float multiplier = sharedPreferences.getFloat("multiplier", 1f);
        Log.d("Step Counter", "Loaded multiplier: " + multiplier);
        return multiplier;
    }

    private int getStreakMultiplier() {
        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        int streakMultiplier = sharedPreferences.getInt("streakMultiplier", 1);
        Log.d("Step Counter", "Loaded streak multiplier: " + streakMultiplier);
        return streakMultiplier;
    }
}