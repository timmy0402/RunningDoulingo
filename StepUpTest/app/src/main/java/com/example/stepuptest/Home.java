package com.example.stepuptest;

import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.stepuptest.databinding.ActivityHomeBinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
//        calendar.set(2025, Calendar.APRIL, 1);
//
//        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d", Locale.getDefault());
//        for (int i = 0; i < 29; i++) {
//            TextView textView = new TextView(this);
//            textView.setTextSize(20f);
//            textView.setPadding(16, 8, 16, 8);
//            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//
//            String formattedDate = dateFormat.format(calendar.getTime());
//            textView.setText(formattedDate);
//
//            dateTimeContainer.addView(textView);
//            calendar.add(Calendar.DAY_OF_MONTH, 1);
//
//        }
        calendar.set(2025, Calendar.MAY, 1);

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d", Locale.getDefault());
        for (int i = 0; i < 29; i--) {
            TextView textView = new TextView(this);
            textView.setTextSize(20f);
            textView.setPadding(16, 8, 16, 8);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            String formattedDate = dateFormat.format(calendar.getTime());
            textView.setText(formattedDate);

            dateTimeContainer.addView(textView);
//            calendar.add(Calendar.DAY_OF_MONTH, 1);

            calendar.add(Calendar.DAY_OF_MONTH, -1);

        }

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
        toolBarLayout.setTitle(getTitle());

    }
}