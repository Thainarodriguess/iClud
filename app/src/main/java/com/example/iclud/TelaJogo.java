package com.example.iclud;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TelaJogo extends AppCompatActivity {

    private TextView txtTempo;

    private GridLayout gridDinamico;

    private Handler handler = new Handler();

    private Runnable runnable;

    private int segundos = 0;

    private int faseAtual;

    private int pontuacao;

    private int acertosNaFase = 0;

    private int totalBlocosDaFase = 0;

    private TextView[] bolhas;

    class Palavra {

        String nome;

        String[] silabas;

        Palavra(String nome, String... silabas) {

            this.nome = nome;

            this.silabas = silabas;
        }
    }

    class Cruzadinha {

        Palavra horizontal;

        Palavra vertical;

        int indiceHorizontal;

        int indiceVertical;


        Cruzadinha(
                Palavra horizontal,
                Palavra vertical,
                int indiceHorizontal,
                int indiceVertical
        ) {

            this.horizontal = horizontal;

            this.vertical = vertical;

            this.indiceHorizontal = indiceHorizontal;

            this.indiceVertical = indiceVertical;
        }
    }

    class Celula {

        int linha;

        int coluna;

        String silaba;


        Celula(
                int linha,
                int coluna,
                String silaba
        ) {

            this.linha = linha;

            this.coluna = coluna;

            this.silaba = silaba;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.tela_jogo);


        txtTempo =
                findViewById(R.id.txtTempo);


        gridDinamico =
                findViewById(R.id.gridDinamico);


        faseAtual =
                getIntent().getIntExtra(
                        "FASE_ATUAL",
                        1
                );


        pontuacao =
                getIntent().getIntExtra(
                        "PONTUACAO",
                        0
                );


        bolhas = new TextView[]{

                findViewById(R.id.bolha1),

                findViewById(R.id.bolha2),

                findViewById(R.id.bolha3),

                findViewById(R.id.bolha4),

                findViewById(R.id.bolha5),

                findViewById(R.id.bolha6),

                findViewById(R.id.bolha7),

                findViewById(R.id.bolha8)
        };


        iniciarCronometro();

        carregarFase();
    }


    private void iniciarCronometro() {

        runnable = new Runnable() {

            @Override
            public void run() {

                segundos++;

                txtTempo.setText(
                        "Tempo: " +
                                segundos +
                                "s"
                );

                handler.postDelayed(
                        this,
                        1000
                );
            }
        };


        handler.postDelayed(
                runnable,
                1000
        );
    }

    private void carregarFase() {

        gridDinamico.removeAllViews();

        acertosNaFase = 0;


        Cruzadinha cruzadinha =
                obterCruzadinhaDaFase(
                        faseAtual
                );


        criarGrade(
                cruzadinha
        );


        carregarBolhas(
                cruzadinha
        );
    }


    private Cruzadinha obterCruzadinhaDaFase(
            int fase
    ) {

        if (fase == 1) {

            return new Cruzadinha(

                    new Palavra(
                            "CASA",
                            "CA",
                            "SA"
                    ),

                    new Palavra(
                            "SAPO",
                            "SA",
                            "PO"
                    ),

                    1,
                    0
            );
        }


        if (fase == 2) {

            return new Cruzadinha(

                    new Palavra(
                            "CAMA",
                            "CA",
                            "MA"
                    ),

                    new Palavra(
                            "MACACO",
                            "MA",
                            "CA",
                            "CO"
                    ),

                    1,
                    0
            );
        }


        if (fase == 3) {

            return new Cruzadinha(

                    new Palavra(
                            "GATO",
                            "GA",
                            "TO"
                    ),

                    new Palavra(
                            "PATO",
                            "PA",
                            "TO"
                    ),

                    1,
                    1
            );
        }


        return new Cruzadinha(

                new Palavra(
                        "ESCOLA",
                        "ES",
                        "CO",
                        "LA"
                ),

                new Palavra(
                        "ESCADA",
                        "ES",
                        "CA",
                        "DA"
                ),

                0,
                0
        );
    }


    private void criarGrade(
            Cruzadinha cruzadinha
    ) {

        List<Celula> celulas =
                new ArrayList<>();


        Palavra horizontal =
                cruzadinha.horizontal;


        Palavra vertical =
                cruzadinha.vertical;


        int cruzamentoHorizontal =
                cruzadinha.indiceHorizontal;


        int cruzamentoVertical =
                cruzadinha.indiceVertical;


        for (
                int i = 0;
                i < horizontal.silabas.length;
                i++
        ) {

            int coluna =
                    i -
                            cruzamentoHorizontal;


            celulas.add(
                    new Celula(
                            0,
                            coluna,
                            horizontal.silabas[i]
                    )
            );
        }


        for (
                int i = 0;
                i < vertical.silabas.length;
                i++
        ) {

            int linha =
                    i -
                            cruzamentoVertical;


            int coluna = 0;


            String silaba =
                    vertical.silabas[i];


            Celula existente =
                    encontrarCelula(
                            celulas,
                            linha,
                            coluna
                    );


            if (existente != null) {

                if (
                        !existente.silaba
                                .equalsIgnoreCase(
                                        silaba
                                )
                ) {

                    throw new IllegalStateException(
                            "As palavras da cruzadinha não possuem sílabas compatíveis."
                    );
                }

            } else {

                celulas.add(
                        new Celula(
                                linha,
                                coluna,
                                silaba
                        )
                );
            }
        }


        int menorLinha = 0;

        int maiorLinha = 0;

        int menorColuna = 0;

        int maiorColuna = 0;


        for (
                Celula celula :
                celulas
        ) {

            menorLinha =
                    Math.min(
                            menorLinha,
                            celula.linha
                    );


            maiorLinha =
                    Math.max(
                            maiorLinha,
                            celula.linha
                    );


            menorColuna =
                    Math.min(
                            menorColuna,
                            celula.coluna
                    );


            maiorColuna =
                    Math.max(
                            maiorColuna,
                            celula.coluna
                    );
        }


        int quantidadeLinhas =
                maiorLinha -
                        menorLinha +
                        1;


        int quantidadeColunas =
                maiorColuna -
                        menorColuna +
                        1;


        gridDinamico.setRowCount(
                quantidadeLinhas
        );


        gridDinamico.setColumnCount(
                quantidadeColunas
        );


        float escala =
                getResources()
                        .getDisplayMetrics()
                        .density;


        int tamanhoBloco =
                (int) (
                        52 * escala
                );


        int margem =
                (int) (
                        3 * escala
                );


        for (
                int linha = menorLinha;
                linha <= maiorLinha;
                linha++
        ) {

            for (
                    int coluna = menorColuna;
                    coluna <= maiorColuna;
                    coluna++
            ) {

                Celula celula =
                        encontrarCelula(
                                celulas,
                                linha,
                                coluna
                        );


                TextView slot =
                        new TextView(this);


                GridLayout.LayoutParams params =
                        new GridLayout.LayoutParams();


                params.width =
                        tamanhoBloco;


                params.height =
                        tamanhoBloco;


                params.setMargins(
                        margem,
                        margem,
                        margem,
                        margem
                );


                params.rowSpec =
                        GridLayout.spec(
                                linha -
                                        menorLinha
                        );


                params.columnSpec =
                        GridLayout.spec(
                                coluna -
                                        menorColuna
                        );


                slot.setLayoutParams(
                        params
                );


                slot.setGravity(
                        Gravity.CENTER
                );


                slot.setTextSize(
                        14
                );


                slot.setTypeface(
                        Typeface.DEFAULT,
                        Typeface.BOLD
                );


                if (celula != null) {

                    slot.setBackgroundColor(
                            Color.WHITE
                    );


                    slot.setTextColor(
                            Color.parseColor(
                                    "#333333"
                            )
                    );


                    configurarSlotDeQueda(
                            slot,
                            celula.silaba
                    );

                } else {

                    slot.setBackgroundColor(
                            Color.TRANSPARENT
                    );


                    slot.setEnabled(false);
                }


                gridDinamico.addView(
                        slot
                );
            }
        }


        totalBlocosDaFase =
                horizontal.silabas.length
                        +
                        vertical.silabas.length
                        -
                        1;
    }

    private Celula encontrarCelula(
            List<Celula> celulas,
            int linha,
            int coluna
    ) {

        for (
                Celula celula :
                celulas
        ) {

            if (
                    celula.linha == linha &&
                            celula.coluna == coluna
            ) {

                return celula;
            }
        }


        return null;
    }

    private void carregarBolhas(
            Cruzadinha cruzadinha
    ) {

        List<String> silabas =
                new ArrayList<>();


        for (
                String silaba :
                cruzadinha.horizontal.silabas
        ) {

            silabas.add(
                    silaba
            );
        }

        for (
                int i = 0;
                i < cruzadinha.vertical.silabas.length;
                i++
        ) {

            if (
                    i !=
                            cruzadinha.indiceVertical
            ) {

                silabas.add(
                        cruzadinha.vertical.silabas[i]
                );
            }
        }


        Collections.shuffle(
                silabas
        );



        for (
                TextView bolha :
                bolhas
        ) {

            bolha.setVisibility(
                    View.GONE
            );

            bolha.setOnTouchListener(
                    null
            );
        }



        for (
                int i = 0;
                i < silabas.size()
                        &&
                        i < bolhas.length;
                i++
        ) {

            TextView bolha =
                    bolhas[i];


            bolha.setVisibility(
                    View.VISIBLE
            );


            bolha.setText(
                    silabas.get(i)
            );


            bolha.setAlpha(
                    1f
            );


            configurarArrasto(
                    bolha
            );
        }
    }

    private void configurarArrasto(
            View bolha
    ) {

        bolha.setOnTouchListener(
                (v, event) -> {

                    if (
                            event.getAction()
                                    ==
                                    MotionEvent.ACTION_DOWN
                    ) {

                        TextView texto =
                                (TextView) v;


                        ClipData data =
                                ClipData.newPlainText(
                                        "silaba",
                                        texto.getText()
                                                .toString()
                                );


                        View.DragShadowBuilder shadowBuilder =
                                new View.DragShadowBuilder(
                                        v
                                );


                        v.startDragAndDrop(
                                data,
                                shadowBuilder,
                                v,
                                0
                        );


                        return true;
                    }


                    return false;
                }
        );
    }

    private void configurarSlotDeQueda(
            TextView slot,
            String silabaCorreta
    ) {

        slot.setOnDragListener(
                (v, event) -> {

                    switch (
                            event.getAction()
                    ) {

                        case DragEvent.ACTION_DRAG_STARTED:

                            return event
                                    .getClipDescription()
                                    != null;


                        case DragEvent.ACTION_DRAG_ENTERED:

                            return true;


                        case DragEvent.ACTION_DRAG_EXITED:

                            return true;


                        case DragEvent.ACTION_DROP:

                            if (
                                    !slot.getText()
                                            .toString()
                                            .isEmpty()
                            ) {

                                return true;
                            }


                            ClipData.Item item =
                                    event.getClipData()
                                            .getItemAt(0);


                            String silabaArrastada =
                                    item.getText()
                                            .toString();


                            if (
                                    silabaArrastada
                                            .equalsIgnoreCase(
                                                    silabaCorreta
                                            )
                            ) {

                                slot.setText(
                                        silabaArrastada
                                );


                                slot.setBackgroundColor(
                                        Color.rgb(
                                                200,
                                                230,
                                                201
                                        )
                                );


                                acertosNaFase++;

                                pontuacao += 10;


                                if (
                                        pontuacao > 100
                                ) {

                                    pontuacao = 100;
                                }


                                View origem =
                                        (View)
                                                event.getLocalState();


                                if (
                                        origem != null
                                ) {

                                    origem.setVisibility(
                                            View.INVISIBLE
                                    );
                                }


                                if (
                                        acertosNaFase
                                                >=
                                                totalBlocosDaFase
                                ) {

                                    concluirFase();
                                }


                            } else {


                            }


                            return true;


                        case DragEvent.ACTION_DRAG_ENDED:

                            return true;
                    }


                    return true;
                }
        );
    }

    private void concluirFase() {

        if (
                handler != null &&
                        runnable != null
        ) {

            handler.removeCallbacks(
                    runnable
            );
        }


        if (
                faseAtual >= 4
        ) {

            Intent intent =
                    new Intent(
                            TelaJogo.this,
                            TelaFinal.class
                    );


            intent.putExtra(
                    "PONTUACAO",
                    pontuacao
            );


            startActivity(
                    intent
            );


            finish();

        } else {


            Intent intent =
                    new Intent(
                            TelaJogo.this,
                            TelaPontuacao.class
                    );


            intent.putExtra(
                    "FASE_COMPLETA",
                    faseAtual
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
    }


    @Override
    protected void onDestroy() {

        super.onDestroy();


        if (
                handler != null &&
                        runnable != null
        ) {

            handler.removeCallbacks(
                    runnable
            );
        }
    }
}