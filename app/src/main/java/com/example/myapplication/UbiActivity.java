package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class UbiActivity extends AppCompatActivity {

    private Button logout;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubi);

        // Inicializar Firebase Authentication
        mAuth = FirebaseAuth.getInstance();

        logout = findViewById(R.id.btnCerrarSesion);

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
}