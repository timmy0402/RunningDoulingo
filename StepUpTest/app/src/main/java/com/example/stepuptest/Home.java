package com.example.stepuptest;

import android.os.Bundle;

import com.example.stepuptest.ui.progress.CircleProgressView;


import androidx.appcompat.app.AppCompatActivity;

import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.stepuptest.databinding.ActivityHomeBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class Home extends AppCompatActivity {

    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // inside onCreate after binding
        LinearLayout dateTimeContainer = findViewById(R.id.dateTimeContainer);

        Calendar calendar = Calendar.getInstance();
        calendar.set(2025, Calendar.APRIL, 27);

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d", Locale.getDefault());


        for (int i = 7; i > 0; i--) {
            String dateKey = dateFormat.format(calendar.getTime());
            String stepsInfo = MainActivity.dailySteps.getOrDefault(dateKey, "0/8000");

            LinearLayout linearDaily = new LinearLayout(this);
            linearDaily.setGravity(Gravity.CENTER_VERTICAL);
            linearDaily.setOrientation(LinearLayout.VERTICAL);

            LinearLayout rowLayout = new LinearLayout(this);
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            rowLayout.setGravity(Gravity.CENTER_VERTICAL);
            rowLayout.setPadding(100,4,100,4);
            if (i % 2 == 0) {
                rowLayout.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            } else {
                rowLayout.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            }

            TextView textView = new TextView(this);
            textView.setTextSize(20f);
            textView.setPadding(0, 8, 0, 8);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            linearDaily.addView(textView);

            String formattedDate = dateFormat.format(calendar.getTime());
            String padding = "  -    -     -    -  ";
            String withPadding = padding + formattedDate + padding;
            textView.setText(withPadding);

            CircleProgressView circleProgressView = new CircleProgressView(this, null);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(200, 200);
            params.setMargins(16, 16, 16, 16);
            circleProgressView.setLayoutParams(params);

            String[] parts = stepsInfo.split("/");
            int steps = Integer.parseInt(parts[0]);
            int goal = Integer.parseInt(parts[1]);
            float progress = Math.min(1f, steps / (float) goal);
            circleProgressView.setProgress(progress);

            rowLayout.addView(circleProgressView);
            TextView tv = new TextView(this);
            tv.setTextSize(20f);
            tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tv.setText(stepsInfo);
            rowLayout.addView(tv);

            linearDaily.addView(rowLayout);
            dateTimeContainer.addView(linearDaily);

            calendar.add(Calendar.DAY_OF_MONTH, -1);
        }

        /*
        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
        toolBarLayout.setTitle(getTitle());

        */

    }
}