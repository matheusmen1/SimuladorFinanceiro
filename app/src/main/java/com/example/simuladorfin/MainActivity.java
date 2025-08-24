package com.example.simuladorfin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private EditText etValor;
    private SeekBar sbPrazo, sbJuros;
    private TextView tvPrazo, tvJuros, tvParcela;

    private double parcela=0,valor,juros;
    private int prazo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().hide();
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //binding com os componentes da interface
        etValor=findViewById(R.id.etValor);
        sbPrazo=findViewById(R.id.sbPrazo);
        sbJuros=findViewById(R.id.sbJuros);
        tvPrazo=findViewById(R.id.tvPrazo);
        tvJuros=findViewById(R.id.tvJuros);
        tvParcela=findViewById(R.id.tvParcela);
        //recuperando os parâmetros do empréstimo armazenados na última utilização do app
        SharedPreferences sharedPreferences=getSharedPreferences("config",MODE_PRIVATE);
        valor=sharedPreferences.getFloat("valor",5000);
        etValor.setText(String.format("%.2f",valor));
        //juros=sharedPreferences.getFloat("juros",5);
        juros = 5;
        tvJuros.setText(String.format("%.2f", juros));
        sbJuros.setProgress((int)(juros*100));
        prazo=sharedPreferences.getInt("prazo",12);
        tvPrazo.setText(""+prazo);
        sbPrazo.setProgress(prazo);
        calcParcela();
        //gerando eventos
        sbPrazo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvPrazo.setText(""+i);
                calcParcela();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sbJuros.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvJuros.setText(""+(i/100.));
                calcParcela();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        etValor.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                calcParcela();
                return false;
            }
        });
        tvParcela.setOnClickListener(e->{trocarActivity();});
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences sharedPreferences=getSharedPreferences("config",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putFloat("valor", (float)valor);
        editor.putFloat("juros",(float)juros);
        editor.putInt("prazo",prazo);
        editor.commit();
    }

    private void trocarActivity() {
        Intent intent = new Intent(this, PlanilhaActivity.class);
        intent.putExtra("valor",valor);
        intent.putExtra("juros",juros);
        intent.putExtra("prazo",prazo);
        startActivity(intent);
    }

    private void calcParcela(){
        if(!etValor.getText().toString().isEmpty())
            valor=Double.parseDouble(etValor.getText().toString().replace(",","."));
        else
            valor=0;
        prazo=sbPrazo.getProgress();
        juros=sbJuros.getProgress()/100.0;
        parcela=Price.calcParcela(valor,juros,prazo);
        tvParcela.setText(String.format("R$ %.2f",parcela));
    }

}