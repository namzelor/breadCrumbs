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

public class MainActivity extends AppCompatActivity implements CompassManager.CompassListener, StepManager.StepListener {
    private ActivityMainBinding binding;
    private FusedLocationProviderClient fusedLocationClient;
    private CompassManager compassManager;
    private StepManager stepManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        compassManager = new CompassManager(this, this);
        stepManager = new StepManager(this, this);

        getLastKnownLocation();

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
        compassManager.start();
        stepManager.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        compassManager.stop();
        stepManager.stop();
    }

    private void getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                // Store these values as a breadcrumb
            }
        });
    }

    @Override
    public void onAzimuthChanged(float azimuth) {
        // Handle azimuth updates from the CompassManager
    }

    @Override
    public void onStepCountUpdated(int steps) {
        // Handle step count updates from the StepManager
    }
}
