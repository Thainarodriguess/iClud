package com.example.iclud;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class TelaFinal extends AppCompatActivity {

    @Override
    protected void onCreate(
            Bundle savedInstanceState
    ) {

        super.onCreate(
                savedInstanceState
        );


        setContentView(
                R.layout.tela_final
        );


        Button btnMenuPrincipal =
                findViewById(
                        R.id.btnMenuPrincipal
                );


        btnMenuPrincipal.setOnClickListener(
                v -> {


                    Intent intent =
                            new Intent(
                                    TelaFinal.this,
                                    TelaPrincipal.class
                            );


                    intent.setFlags(

                            Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    |
                                    Intent.FLAG_ACTIVITY_NEW_TASK
                    );


                    startActivity(
                            intent
                    );


                    finish();
                }
        );
    }
}