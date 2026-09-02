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
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TelaJogo extends AppCompatActivity {

    private TextView txtTempo;
    private TextView txtNivelJogo;
    private GridLayout gridDinamico;

    private final Handler handler = new Handler();
    private Runnable runnable;

    private int segundos = 0;
    private int faseAtual;
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

        Palavra horizontalPrincipal;
        Palavra vertical;
        Palavra horizontalSecundaria;

        int indiceHorizontalPrincipal;
        int indiceVerticalPrincipal;

        int indiceHorizontalSecundaria;
        int indiceVerticalSecundaria;

        Cruzadinha(
                Palavra horizontalPrincipal,
                Palavra vertical,
                Palavra horizontalSecundaria,
                int indiceHorizontalPrincipal,
                int indiceVerticalPrincipal,
                int indiceHorizontalSecundaria,
                int indiceVerticalSecundaria
        ) {

            this.horizontalPrincipal = horizontalPrincipal;
            this.vertical = vertical;
            this.horizontalSecundaria = horizontalSecundaria;

            this.indiceHorizontalPrincipal = indiceHorizontalPrincipal;
            this.indiceVerticalPrincipal = indiceVerticalPrincipal;

            this.indiceHorizontalSecundaria = indiceHorizontalSecundaria;
            this.indiceVerticalSecundaria = indiceVerticalSecundaria;
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

        txtTempo = findViewById(R.id.txtTempo);

        txtNivelJogo =
                findViewById(
                        R.id.txt_nivel_jogo
                );

        gridDinamico =
                findViewById(
                        R.id.gridDinamico
                );

        faseAtual =
                getIntent()
                        .getIntExtra(
                                "FASE_ATUAL",
                                1
                        );

        txtNivelJogo.setText(
                "Nível " + faseAtual
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

        segundos = 0;

        txtTempo.setText(
                "Tempo: 0s"
        );

        runnable = new Runnable() {

            @Override
            public void run() {

                segundos++;

                txtTempo.setText(
                        "Tempo: "
                                +
                                segundos
                                +
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

        List<Celula> celulas =
                criarCelulas(
                        cruzadinha
                );

        criarGrade(
                celulas
        );

        carregarBolhas(
                celulas
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

                    new Palavra(
                            "POTE",
                            "PO",
                            "TE"
                    ),

                    1,
                    0,
                    0,
                    1
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

                    new Palavra(
                            "COPO",
                            "CO",
                            "PO"
                    ),

                    1,
                    0,
                    0,
                    2
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

                    new Palavra(
                            "PANO",
                            "PA",
                            "NO"
                    ),

                    1,
                    1,
                    0,
                    0
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

                new Palavra(
                        "DADO",
                        "DA",
                        "DO"
                ),

                0,
                0,
                0,
                2
        );
    }

    private List<Celula> criarCelulas(
            Cruzadinha cruzadinha
    ) {

        List<Celula> celulas =
                new ArrayList<>();

        for (
                int i = 0;
                i
                        <
                        cruzadinha
                                .horizontalPrincipal
                                .silabas
                                .length;
                i++
        ) {

            adicionarCelula(

                    celulas,

                    0,

                    i
                            -
                            cruzadinha
                                    .indiceHorizontalPrincipal,

                    cruzadinha
                            .horizontalPrincipal
                            .silabas[i]
            );
        }

        for (
                int i = 0;
                i
                        <
                        cruzadinha
                                .vertical
                                .silabas
                                .length;
                i++
        ) {

            adicionarCelula(

                    celulas,

                    i
                            -
                            cruzadinha
                                    .indiceVerticalPrincipal,

                    0,

                    cruzadinha
                            .vertical
                            .silabas[i]
            );
        }

        int linhaSecundaria =
                cruzadinha.indiceVerticalSecundaria
                        -
                        cruzadinha.indiceVerticalPrincipal;

        for (
                int i = 0;
                i
                        <
                        cruzadinha
                                .horizontalSecundaria
                                .silabas
                                .length;
                i++
        ) {

            adicionarCelula(

                    celulas,

                    linhaSecundaria,

                    i
                            -
                            cruzadinha
                                    .indiceHorizontalSecundaria,

                    cruzadinha
                            .horizontalSecundaria
                            .silabas[i]
            );
        }

        return celulas;
    }

    private void adicionarCelula(
            List<Celula> celulas,
            int linha,
            int coluna,
            String silaba
    ) {

        Celula existente =
                encontrarCelula(
                        celulas,
                        linha,
                        coluna
                );

        if (existente != null) {

            if (
                    !existente
                            .silaba
                            .equalsIgnoreCase(
                                    silaba
                            )
            ) {

                throw new IllegalStateException(
                        "As palavras da cruzadinha não possuem sílabas compatíveis."
                );
            }

            return;
        }

        celulas.add(
                new Celula(
                        linha,
                        coluna,
                        silaba
                )
        );
    }

    private void criarGrade(
            List<Celula> celulas
    ) {

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
                maiorLinha
                        -
                        menorLinha
                        +
                        1;

        int quantidadeColunas =
                maiorColuna
                        -
                        menorColuna
                        +
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
                        60
                                *
                                escala
                );

        int margem =
                (int) (
                        4
                                *
                                escala
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
                        new TextView(
                                this
                        );

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
                                linha
                                        -
                                        menorLinha
                        );

                params.columnSpec =
                        GridLayout.spec(
                                coluna
                                        -
                                        menorColuna
                        );

                slot.setLayoutParams(
                        params
                );

                slot.setGravity(
                        Gravity.CENTER
                );

                slot.setTextSize(
                        16
                );

                slot.setTypeface(
                        Typeface.DEFAULT,
                        Typeface.BOLD
                );

                if (
                        celula
                                !=
                                null
                ) {

                    slot.setBackgroundResource(
                            R.drawable.fundo_slot
                    );

                    slot.setTextColor(

                            ContextCompat.getColor(
                                    this,
                                    R.color.titulo
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

                    slot.setEnabled(
                            false
                    );
                }

                gridDinamico.addView(
                        slot
                );
            }
        }

        totalBlocosDaFase =
                celulas.size();
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
                    celula.linha
                            ==
                            linha
                            &&
                            celula.coluna
                                    ==
                                    coluna
            ) {

                return celula;
            }
        }

        return null;
    }

    private void carregarBolhas(
            List<Celula> celulas
    ) {

        List<String> silabas =
                new ArrayList<>();

        for (
                Celula celula :
                celulas
        ) {

            silabas.add(
                    celula.silaba
            );
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
                                        texto
                                                .getText()
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
                                    !=
                                    null;

                        case DragEvent.ACTION_DRAG_ENTERED:

                            return true;

                        case DragEvent.ACTION_DRAG_EXITED:

                            return true;

                        case DragEvent.ACTION_DROP:

                            if (
                                    !slot
                                            .getText()
                                            .toString()
                                            .isEmpty()
                            ) {

                                return true;
                            }

                            String silabaArrastada =
                                    event
                                            .getClipData()
                                            .getItemAt(0)
                                            .getText()
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

                                slot.setBackgroundResource(
                                        R.drawable.fundo_slot_correto
                                );

                                acertosNaFase++;

                                View origem =
                                        (View)
                                                event
                                                        .getLocalState();

                                if (
                                        origem
                                                !=
                                                null
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
                            }

                            return true;

                        case DragEvent.ACTION_DRAG_ENDED:

                            return true;
                    }

                    return true;
                }
        );
    }

    private int calcularPontuacao() {

        switch (
                faseAtual
        ) {

            case 1:
                return 70;

            case 2:
                return 80;

            case 3:
                return 90;

            case 4:
                return 100;

            default:
                return 0;
        }
    }

    private void concluirFase() {

        if (
                runnable
                        !=
                        null
        ) {

            handler.removeCallbacks(
                    runnable
            );
        }

        int pontuacao =
                calcularPontuacao();

        if (
                faseAtual
                        >=
                        4
        ) {

            Intent intent =
                    new Intent(
                            TelaJogo.this,
                            TelaFinal.class
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
                runnable
                        !=
                        null
        ) {

            handler.removeCallbacks(
                    runnable
            );
        }
    }
}