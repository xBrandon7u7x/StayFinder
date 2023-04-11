package com.example.myapplication;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    private Button cerrarsesion, encontrarubi;
    private FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Inicializar Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        cerrarsesion = findViewById(R.id.btncerrarsesion);
        encontrarubi = findViewById(R.id.btnencotrarubic);

        encontrarubi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navegar a la RegisterActivity
                Intent intent = new Intent(HomeActivity.this, UbiActivity.class);
                startActivity(intent);
            }
        });
        cerrarsesion.setOnClickListener(v -> {
            // Navegar a la RegisterActivity
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(intent);
        });

    }

    private void cerrarSesion() {
        // Cerrar sesión en Firebase Authentication
        mAuth.signOut();

        // Regresar a la actividad de inicio de sesión
        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Cerrar la actividad actual para evitar volver atrás con el botón de retroceso
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}