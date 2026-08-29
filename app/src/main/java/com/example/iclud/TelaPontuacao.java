package com.example.iclud;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TelaPontuacao extends AppCompatActivity {

    private TextView txtMensagem;

    private TextView txtPontuacao;

    private Button btnProximaFase;

    private int faseCompleta;

    private int pontuacao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.tela_pontuacao
        );


        txtMensagem =
                findViewById(
                        R.id.txtMensagem
                );


        txtPontuacao =
                findViewById(
                        R.id.txtPontuacao
                );


        btnProximaFase =
                findViewById(
                        R.id.btnProximaFase
                );


        faseCompleta =
                getIntent().getIntExtra(
                        "FASE_COMPLETA",
                        1
                );


        pontuacao =
                getIntent().getIntExtra(
                        "PONTUACAO",
                        0
                );


        txtMensagem.setText(
                "Parabéns!\nVocê passou do nível "
                        +
                        faseCompleta
                        +
                        "!"
        );


        txtPontuacao.setText(
                "Sua pontuação\n"
                        +
                        pontuacao
                        +
                        " pontos"
        );


        btnProximaFase.setOnClickListener(
                v -> {

                    int proximaFase =
                            faseCompleta + 1;


                    Intent intent =
                            new Intent(
                                    TelaPontuacao.this,
                                    TelaJogo.class
                            );


                    intent.putExtra(
                            "FASE_ATUAL",
                            proximaFase
                    );


                    intent.putExtra(
                            "PONTUACAO",
                            pontuacao
                    );


                    startActivity(
                            intent
                    );


                    finish();
                }
        );
    }
}