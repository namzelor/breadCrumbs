package com.example.breadcrumbs;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class StepManager implements SensorEventListener {
    private final SensorManager sensorManager;
    private final Sensor stepSensor;
    private final StepListener listener;
    private int stepCount;

    public StepManager(Context context, StepListener listener) {
        this.sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        this.stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        this.listener = listener;
    }

    public void start() {
        if (stepSensor != null) {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    public void stop() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            stepCount = (int) event.values[0];
            listener.onStepCountUpdated(stepCount);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    public interface StepListener {
        void onStepCountUpdated(int steps);
    }
}
