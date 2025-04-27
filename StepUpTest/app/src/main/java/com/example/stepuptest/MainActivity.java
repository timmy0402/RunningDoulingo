package com.example.stepuptest;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    // Declare sensor and sensor manager
    private SensorManager sensorManager;
    private Sensor stepSensor;

    // Step counting variables
    private boolean running = false;
    private float totalSteps = 0f;
    private float previousTotalSteps = 0f;

    private float nextGoal = 50f;

    // UI Component
    private TextView stepsTakenTextView;

    private TextView stepGoalTextView;

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
            if (remainingSteps < -20) {
                nextGoal = truncate((int)(50 * Math.sqrt(nextGoal + 2)), 2);
            }
            stepGoalTextView.setText(goal);
        }
    }

    private float truncate(int num, int degree) {
        return (int) (Math.floor(num / Math.pow(10.0, degree)) * ((int) Math.pow(10, degree)));
    }

    // Handle resume event
    @Override
    protected void onResume() {
        super.onResume();
        running = true;
        registerSensor();
    }

    // Handle pause event
    @Override
    protected void onPause() {
        super.onPause();
        running = false;
    }

    // Handle long press to reset steps
    private void resetSteps() {
        stepsTakenTextView.setOnClickListener(v ->
                Toast.makeText(MainActivity.this, "Long press to reset steps", Toast.LENGTH_SHORT).show()
        );

        stepsTakenTextView.setOnLongClickListener(v -> {
            previousTotalSteps = totalSteps;
            stepsTakenTextView.setText("0");
            saveData();
            return true;
        });
    }

    // Save step count
    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("key1", previousTotalSteps);
        editor.apply();
        Log.d("Step Counter", "Steps saved: " + previousTotalSteps);
    }

    // Load step count
    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        previousTotalSteps = sharedPreferences.getFloat("key1", 0f);
        Log.d("Step Counter", "Loaded steps: " + previousTotalSteps);
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
