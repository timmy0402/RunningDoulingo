package com.example.stepuptest;

import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Rank extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RankAdapter adapter;
    private List<RankItem> rankList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);

        recyclerView = findViewById(R.id.rank_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadDailySteps();

        adapter = new RankAdapter(rankList);
        recyclerView.setAdapter(adapter);

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
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