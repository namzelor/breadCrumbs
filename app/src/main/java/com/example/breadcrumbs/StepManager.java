package com.example.breadcrumbs;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class StepManager implements SensorEventListener {
    // Manages access to the device's step counter sensor
    private final SensorManager sensorManager;
    private final Sensor stepSensor;
    private final StepListener listener;
    private int stepCount;

    // Constructor that sets up the sensor manager and step counter sensor
    public StepManager(Context context, StepListener listener) {
        this.sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        this.stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        this.listener = listener;
    }

    // Start listening for step count updates
    public void start() {
        if (stepSensor != null) {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    // Stop listening for step count updates
    public void stop() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Update the step count when a new step is detected
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            stepCount = (int) event.values[0];
            // Notify the listener of the updated step count
            listener.onStepCountUpdated(stepCount);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    // Interface to communicate step count updates back to the activity
    public interface StepListener {
        void onStepCountUpdated(int steps);
    }
}