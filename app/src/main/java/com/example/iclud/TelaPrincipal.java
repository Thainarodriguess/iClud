package com.example.iclud;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class TelaPrincipal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tela_principal);

        Button btnIniciar = findViewById(R.id.btnIniciar);
        Button btnSobre = findViewById(R.id.btnSobre);

        btnIniciar.setOnClickListener(v -> {

            Intent intent = new Intent(
                    TelaPrincipal.this,
                    TelaJogo.class
            );

            intent.putExtra("FASE_ATUAL", 1);
            intent.putExtra("PONTUACAO", 0);

            startActivity(intent);
        });

        btnSobre.setOnClickListener(v -> {

            Intent intent = new Intent(
                    TelaPrincipal.this,
                    TelaSobre.class
            );

            startActivity(intent);
        });
    }
}