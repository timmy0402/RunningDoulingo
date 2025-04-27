package com.example.stepuptest;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Rank extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_rank);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        BottomNavigationView bottomNavigationView=findViewById(R.id.nav_view);
        bottomNavigationView.setSelectedItemId(R.id.rank);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.navigation_home) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityIfNeeded(intent,0);
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.power_up) {
                Intent intent = new Intent(getApplicationContext(), PowerUp.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityIfNeeded(intent,0);
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.login) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityIfNeeded(intent,0);
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.rank) {
                return true;
            }
            return false;
        });
    }
    private void loadDailySteps() {
        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        String storedHash = sharedPreferences.getString("hashString", null);
        rankList = new ArrayList<>();

        if (storedHash != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<HashMap<String, Integer>>() {}.getType();
            HashMap<String, Integer> dailySteps = gson.fromJson(storedHash, type);

            for (String date : dailySteps.keySet()) {
                int steps = dailySteps.get(date);
                rankList.add(new RankItem(date, steps));
            }
        }
    }
}