package com.example.iclud;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class TelaPontuacao extends AppCompatActivity {

    private TextView txtMensagem;

    private TextView txtNivel;

    private TextView txtPontuacao;

    private TextView estrela1;

    private TextView estrela2;

    private TextView estrela3;

    private TextView estrela4;

    private TextView estrela5;

    private Button btnProximaFase;

    private int faseCompleta;

    private int pontuacao;


    @Override
    protected void onCreate(
            Bundle savedInstanceState
    ) {

        super.onCreate(
                savedInstanceState
        );


        setContentView(
                R.layout.tela_pontuacao
        );


        txtMensagem =
                findViewById(
                        R.id.txtMensagem
                );


        txtNivel =
                findViewById(
                        R.id.txtNivel
                );


        txtPontuacao =
                findViewById(
                        R.id.txtPontuacao
                );


        estrela1 =
                findViewById(
                        R.id.estrela1
                );


        estrela2 =
                findViewById(
                        R.id.estrela2
                );


        estrela3 =
                findViewById(
                        R.id.estrela3
                );


        estrela4 =
                findViewById(
                        R.id.estrela4
                );


        estrela5 =
                findViewById(
                        R.id.estrela5
                );


        btnProximaFase =
                findViewById(
                        R.id.btnProximaFase
                );


        faseCompleta =
                getIntent()
                        .getIntExtra(
                                "FASE_COMPLETA",
                                1
                        );


        pontuacao =
                getIntent()
                        .getIntExtra(
                                "PONTUACAO",
                                70
                        );


        txtMensagem.setText(
                "Parabéns! Você passou do"
        );


        txtNivel.setText(
                "Nível "
                        +
                        faseCompleta
        );


        txtPontuacao.setText(
                "Pontuação: "
                        +
                        pontuacao
        );


        atualizarEstrelas();


        btnProximaFase.setOnClickListener(
                v -> {


                    int proximaFase =
                            faseCompleta
                                    +
                                    1;


                    Intent intent =
                            new Intent(
                                    TelaPontuacao.this,
                                    TelaJogo.class
                            );


                    intent.putExtra(
                            "FASE_ATUAL",
                            proximaFase
                    );


                    startActivity(
                            intent
                    );


                    finish();
                }
        );
    }



    private void atualizarEstrelas() {


        int corAtiva =
                ContextCompat.getColor(
                        this,
                        R.color.estrela_ativa
                );


        int corInativa =
                ContextCompat.getColor(
                        this,
                        R.color.estrela_inativa
                );


        estrela1.setTextColor(
                corInativa
        );


        estrela2.setTextColor(
                corInativa
        );


        estrela3.setTextColor(
                corInativa
        );


        estrela4.setTextColor(
                corInativa
        );


        estrela5.setTextColor(
                corInativa
        );


        if (
                pontuacao
                        >=
                        70
        ) {

            estrela1.setTextColor(
                    corAtiva
            );

            estrela2.setTextColor(
                    corAtiva
            );

            estrela3.setTextColor(
                    corAtiva
            );
        }


        if (
                pontuacao
                        >=
                        80
        ) {

            estrela4.setTextColor(
                    corAtiva
            );
        }


        if (
                pontuacao
                        >=
                        100
        ) {

            estrela5.setTextColor(
                    corAtiva
            );
        }
    }
}