package com.example.stepuptest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class Rank extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RankAdapter adapter;
    private List<RankItem> rankList;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);

        recyclerView = findViewById(R.id.rank_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadDailySteps();

        adapter = new RankAdapter(rankList);
        recyclerView.setAdapter(adapter);

        bottomNavigationView = findViewById(R.id.nav_view);
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

    @Override
    protected void onStart(){
        loadDailySteps();
        super.onStart();

        adapter = new RankAdapter(rankList);
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        loadDailySteps();
        super.onResume();

        bottomNavigationView.setSelectedItemId(R.id.rank);
        adapter = new RankAdapter(rankList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onPause() {
        loadDailySteps();
        super.onPause();


        adapter = new RankAdapter(rankList);
        recyclerView.setAdapter(adapter);
    }


    private void loadDailySteps() {
        Intent intent = getIntent();
        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", MODE_PRIVATE);
        float totalSteps = sharedPreferences.getFloat("key2_total_step", 0f);
        float previousTotalSteps = sharedPreferences.getFloat("key1", 0f);

        Log.d("Step Counter", "Loaded total steps : " + totalSteps);
        Log.d("Step Counter", "Loaded steps: " + previousTotalSteps);


        rankList = new ArrayList<>();

        rankList.add(new RankItem("Tim", 999));
        rankList.add(new RankItem("Anoop", 134));
        rankList.add(new RankItem("Dudu", 20));
        rankList.add(new RankItem("Mario", 1));
        rankList.add(new RankItem("MyKoala", (int) (totalSteps - previousTotalSteps)));

        rankList.sort(RankItem::compareTo);
    }

}