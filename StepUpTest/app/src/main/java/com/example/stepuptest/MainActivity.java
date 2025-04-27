package com.example.stepuptest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.time.LocalDate;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    // Declare sensor and sensor manager
    private SensorManager sensorManager;
    private Sensor stepSensor;

    // Step counting variables
    private boolean running = false;
    private float totalSteps = 0f;
    private float previousTotalSteps = 0f;
    private float yesterdaySteps = 0f;
    private HashMap<String, Integer> dailySteps;
    private float nextGoal = 50f;
    private float dailyGoal = 10f;
    private long dateTestLong = 0;
    private long currency = 0;
    private long streak = 0;
    private boolean finishedStreak = false;
    private boolean finishedGoal = false;


    static IntentFilter s_intentFilter;

    static {
        s_intentFilter = new IntentFilter();
        s_intentFilter.addAction(Intent.ACTION_TIME_TICK);
        s_intentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        s_intentFilter.addAction(Intent.ACTION_TIME_CHANGED);
    }

    // UI Component
    private TextView stepsTakenTextView;

    private TextView stepGoalTextView;

    private TextView dateTextView;

    private TextView dailyStepsTextView;

    private TextView currencyTextView;

    private static final int PERMISSION_REQUEST_ACTIVITY_RECOGNITION = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check and request permission for Activity Recognition (Android 10+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (checkSelfPermission(android.Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.ACTIVITY_RECOGNITION}, PERMISSION_REQUEST_ACTIVITY_RECOGNITION);
            }
        }

        // Define text view for step count
        stepsTakenTextView = findViewById(R.id.tv_stepsTaken);

        stepGoalTextView = findViewById(R.id.tv_stepGoal);

        dateTextView = findViewById(R.id.tv_date);

        dailyStepsTextView = findViewById(R.id.tv_dailySteps);
        currencyTextView = findViewById(R.id.tv_currency);

        // Define sensor and sensor manager
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        }

        // Check if sensor is available
        if (stepSensor == null) {
            Toast.makeText(this, "No Step Counter Sensor found!", Toast.LENGTH_LONG).show();
        }

        loadData();
        resetSteps();
        testDate();
    }

    // Register sensor when activity starts
    @Override
    protected void onStart() {
        super.onStart();
        registerSensor();
    }

    // Unregister sensor when activity stops
    @Override
    protected void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this);
    }

    // Register step counter sensor
    private void registerSensor() {
        if (stepSensor != null) {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI);
            Log.d("Step Counter", "Step Sensor Registered");
        }
    }

    // Handle step counter changes
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event == null) return;

        Log.d("Step Counter", "Sensor event received: " + event.values[0]);

        // Update step count
        if (running) {
            totalSteps = event.values[0];
            int currentSteps = (int) (totalSteps - previousTotalSteps);
            stepsTakenTextView.setText(String.valueOf(currentSteps));

            int remainingSteps = (int) (nextGoal - currentSteps);
            String goal = (remainingSteps > 0) ? Integer.toString(currentSteps) + "/" + Integer.toString((int)nextGoal) : "Achieved!";
            if (remainingSteps < -10) {
                currency += (10 * streak / 10);
                currencyTextView.setText(String.valueOf(currency));
                nextGoal = truncate((int)(50 * Math.sqrt(nextGoal + 2)), 2);
                finishedGoal = true;
            }
            stepGoalTextView.setText(goal);

            int todaySteps = (int) (currentSteps - yesterdaySteps);
            int dailyRemaining = (int) (dailyGoal - todaySteps);
            String currGoal = (dailyRemaining > 0) ? Integer.toString(todaySteps) + "/" + Integer.toString((int) dailyGoal) : "Streak! " + Long.toString(streak);
            if (dailyRemaining <= 0 && !finishedStreak) {
                currency += streak;
                ++streak;
                currencyTextView.setText(String.valueOf(currency));
                finishedStreak = true;
            }
            dailyStepsTextView.setText(currGoal);
        }
    }

    private final BroadcastReceiver m_timeChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(Intent.ACTION_TIME_CHANGED) ||
                    action.equals(Intent.ACTION_TIMEZONE_CHANGED)) {
                changeDate();
            }
        }
    };

    private float truncate(int num, int degree) {
        return (int) (Math.floor(num / Math.pow(10.0, degree)) * ((int) Math.pow(10, degree)));
    }

    private void changeDate() {
        int today = (int) (((int) (totalSteps - previousTotalSteps)) - yesterdaySteps);
        currency += today / (long) Math.pow(10, 1);
        dailySteps.put(LocalDate.now().plusDays(dateTestLong).toString(), today);
        dateTextView.setText(String.valueOf(LocalDate.now().plusDays(dateTestLong)));
        currencyTextView.setText(String.valueOf(currency));
        dailyStepsTextView.setText("0");
        if (finishedStreak) {
            finishedStreak = false;
        }
        if (finishedGoal) {
            dailyGoal += 10f;
            finishedGoal = false;
        }
        yesterdaySteps = totalSteps - previousTotalSteps;
    }

    // Handle resume event
    @Override
    protected void onResume() {
        super.onResume();
        running = true;
        registerSensor();
        registerReceiver(m_timeChangedReceiver, s_intentFilter);
        dateTextView.setText(String.valueOf(LocalDate.now()));
    }

    // Handle pause event
    @Override
    protected void onPause() {
        super.onPause();
        running = false;
        unregisterReceiver(m_timeChangedReceiver);
    }

    // Handle long press to reset steps
    private void resetSteps() {
        stepsTakenTextView.setOnClickListener(v ->
                Toast.makeText(MainActivity.this, "Long press to reset steps", Toast.LENGTH_SHORT).show()
        );

        stepsTakenTextView.setOnLongClickListener(v -> {
            previousTotalSteps = totalSteps;
            stepsTakenTextView.setText("0");
            dailyStepsTextView.setText("0");
            nextGoal = 50f;
            dailyGoal = 10f;
            saveData();
            return true;
        });
    }

    private void testDate() {
        stepsTakenTextView.setOnClickListener(v -> {
            ++dateTestLong;
            changeDate();
        });
    }

    // Save step count
    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String hashMapString = gson.toJson(dailySteps);
        editor.putString("hashString", hashMapString);
        editor.putFloat("key1", previousTotalSteps);
        editor.putLong("currency", currency);
        editor.putLong("streak", streak);
        editor.apply();
        Log.d("Step Counter", "Steps saved: " + previousTotalSteps);
        Log.d("Step Counter", "Saved daily steps");
        Log.d("Step Counter", "Saved currency: " + currency);
        Log.d("Step Counter", "Saved streak: " + streak);
    }

    // Load step count
    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        previousTotalSteps = sharedPreferences.getFloat("key1", 0f);
        currency = sharedPreferences.getLong("currency", 0);
        streak = sharedPreferences.getLong("streak", 0);
        String storedHash = sharedPreferences.getString("hashString", "River stone");
        java.lang.reflect.Type type = new TypeToken<HashMap<String, Integer>>(){}.getType();
        if (!storedHash.equals("River stone")) {
            Gson gson = new Gson();
            dailySteps = gson.fromJson(storedHash, type);
        } else {
            dailySteps  = new HashMap<>();
        }
        Log.d("Step Counter", "Loaded steps: " + previousTotalSteps);
        Log.d("Step Counter", "Loaded daily steps");
        Log.d("Step Counter", "Loaded currency: " + currency);
        Log.d("Step Counter", "Loaded streak: " + streak);
    }

    // Handle permission request results
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_ACTIVITY_RECOGNITION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
