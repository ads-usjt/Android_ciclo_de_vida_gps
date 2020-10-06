package dev.lucasliet.android_ciclo_de_vida_gps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private LocationManager locationManager;
    private LocationListener locationListener;
    private TextView locationTextView;
    private double latitudeAtual;
    private double longitudeAtual;
    private static final int GPS_REQUEST_PERMISSION_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationTextView = findViewById(R.id.locationTextView);

        locationListener = location -> {
            double lat = location.getLatitude();
            double lon = location.getLongitude();
            latitudeAtual = lat;
            longitudeAtual = lon;
            locationTextView.setText(
                    String.format(new Locale("pt-BR"),"Lat: %f, Long: %f", lat, lon)
            );
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(
                ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
        ){
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    5000,
                    10,
                    locationListener
            );
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    GPS_REQUEST_PERMISSION_CODE
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == GPS_REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (
                        ActivityCompat.checkSelfPermission(
                                this,
                                Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                ) {
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            5000,
                            10,
                            locationListener
                    );
                }
            } else {
                Toast.makeText(this, R.string.no_gps_no_app, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        locationManager.removeUpdates(locationListener);
    }
}