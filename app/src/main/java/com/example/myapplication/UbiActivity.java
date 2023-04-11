package com.example.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;

public class UbiActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Button logout;
    private FirebaseAuth mAuth;
    private GoogleMap mMap;
    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubi);

        // Inicializar Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        logout = findViewById(R.id.btnCerrarSesion);
        mapView = findViewById(R.id.map);


        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this); // Registrar el callback para obtener el mapa cuando esté listo

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarSesion();
            }
        });

    }

    private void cerrarSesion() {
        // Cerrar sesión en Firebase Authentication
        mAuth.signOut();

        // Regresar a la actividad de inicio de sesión
        Intent intent = new Intent(UbiActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Cerrar la actividad actual para evitar volver atrás con el botón de retroceso
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Habilitar la capa de ubicación en el mapa
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }

        // Obtener la ubicación actual del dispositivo y mover la cámara a esa ubicación
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location != null) {
            double lat = location.getLatitude();
            double lon = location.getLongitude();
            LatLng latLng = new LatLng(lat, lon);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}