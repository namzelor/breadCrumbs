package com.example.breadcrumbs;

import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.breadcrumbs.databinding.ActivityMainBinding;
import com.example.breadcrumbs.CompassManager;
import com.example.breadcrumbs.StepManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.location.Location;

public class MainActivity extends AppCompatActivity
        implements CompassManager.CompassListener, StepManager.StepListener {

    // UI binding for easier view management
    private ActivityMainBinding binding;

    // Location services for accessing the user's current location
    private FusedLocationProviderClient fusedLocationClient;

    // Compass and Step managers for tracking direction and steps
    private CompassManager compassManager;
    private StepManager stepManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout using view binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize location services
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Initialize the Compass and Step Managers
        compassManager = new CompassManager(this, this);
        stepManager = new StepManager(this, this);

        // Get the user's last known location (if permissions are granted)
        getLastKnownLocation();

        // Set up bottom navigation with navigation controller
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Start listening to compass and step updates when the app resumes
        compassManager.start();
        stepManager.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop listening to sensors when the app is paused to save battery
        compassManager.stop();
        stepManager.stop();
    }

    private void getLastKnownLocation() {
        // Check for location permissions before accessing the user's location
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider requesting location permissions from the user
            return;
        }

        // Fetch the last known location
        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                // Store these coordinates as a breadcrumb
            }
        });
    }

    // Callback method for compass updates
    @Override
    public void onAzimuthChanged(float azimuth) {
        // Handle azimuth changes (e.g., update UI with new direction)
    }

    // Callback method for step count updates
    @Override
    public void onStepCountUpdated(int steps) {
        // Handle step count changes (e.g., store or display the steps)
    }
}