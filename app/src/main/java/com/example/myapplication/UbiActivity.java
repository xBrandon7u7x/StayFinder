package com.example.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UbiActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Button backtohome;
    private GoogleMap mMap;
    private MapView mapView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubi);

        mapView = findViewById(R.id.map);
        backtohome = findViewById(R.id.btnbacktohome);

        backtohome.setOnClickListener(v -> {
            // Navegar a la RegisterActivity
            Intent intent = new Intent(UbiActivity.this, HomeActivity.class);
            startActivity(intent);
        });

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this); // Registrar el callback para obtener el mapa cuando esté listo
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

            // Obtener hoteles cercanos
            String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + lat + "," + lon +
                    "&radius=5000&type=lodging&key=AIzaSyDE26FAH_KAqBBzzrBRPKcad3H6R5sEGN4"; // Reemplazar YOUR_API_KEY con tu propia clave de API de Google Places

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    response -> {
                        try {
                            JSONArray results = response.getJSONArray("results");
                            for (int i = 0; i < results.length(); i++) {
                                JSONObject result = results.getJSONObject(i);
                                JSONObject geometry = result.getJSONObject("geometry");
                                JSONObject locationObj = geometry.getJSONObject("location");
                                double hotelLat = locationObj.getDouble("lat");
                                double hotelLon = locationObj.getDouble("lng");
                                String name = result.getString("name");

                                // Agregar marcadores en el mapa para los hoteles encontrados
                                LatLng hotelLatLng = new LatLng(hotelLat, hotelLon);
                                mMap.addMarker(new MarkerOptions().position(hotelLatLng).title(name));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    error -> {
                        Toast.makeText(UbiActivity.this, "Error al obtener hoteles cercanos.", Toast.LENGTH_SHORT).show();
                    });

            // Agregar la solicitud a la cola de solicitudes de Volley
            Volley.newRequestQueue(this).add(request);
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