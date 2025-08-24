package com.example.simuladorfin;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class PlanilhaActivity extends AppCompatActivity {
    private TextView tvValor, tvJuros2, tvTotalJuros;
    private ListView listView;
    private View footer;
    private double parcela=0,valor,juros, amort, parcelaSacre = 0, totalJuros = 0;
    private int prazo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_planilha);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        tvValor=findViewById(R.id.tvValor);
        tvJuros2=findViewById(R.id.tvJuros2);
        listView=findViewById(R.id.listView);
        valor=getIntent().getDoubleExtra("valor",0);
        juros=getIntent().getDoubleExtra("juros",0);
        prazo=getIntent().getIntExtra("prazo",0);
        tvValor.setText(""+valor);
        tvJuros2.setText(""+juros);
        parcela=Price.calcParcela(valor,juros,prazo);
        listView.addHeaderView(getLayoutInflater().inflate(R.layout.header_layout,listView,false));
        gerarPlanilhaPrice(); // default
        // adicionando um footer a esse ListView, atraves de um View.
        footer = getLayoutInflater().inflate(R.layout.footer_layout, listView, false);
        tvTotalJuros = footer.findViewById(R.id.tvTotalJuros);
        tvTotalJuros.setText(String.format("%.2f", totalJuros));
        listView.addFooterView(footer);

        //gerando evento para o listview
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Parcela parcela1=(Parcela)adapterView.getItemAtPosition(i);
                Toast.makeText(PlanilhaActivity.this,"Valor dos juros: R$ "+
                        String.format("%.2f",parcela1.getJuros())+
                        "\nValor a deduzir R$ "+
                        String.format("%.2f",parcela1.getAmort()),Toast.LENGTH_LONG)
                        .show();
                //Snackbar snackbar;
                //snackbar=Snackbar.make(view,"Valor dos juros: R$ "+
                //        String.format("%.2f",parcela1.getJuros())+
                //        " Valor a deduzir R$ "+
                //String.format("%.2f",parcela1.getAmort()),
                  //      Snackbar.LENGTH_LONG);
                //snackbar.show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_planilha,menu);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Simulador Financeiro");
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()== R.id.it_sacre)
        {
            gerarPlanilhaSacre();
            tvTotalJuros.setText(String.format("%.2f", totalJuros));
            Toast.makeText(this,"Acessou a opção SACRE",Toast.LENGTH_LONG).show();
        }
        if (item.getItemId()== R.id.it_price){

            gerarPlanilhaPrice();
            tvTotalJuros.setText(String.format("%.2f", totalJuros));
            Toast.makeText(this,"Acessou a opção PRICE",Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void gerarPlanilhaPrice() {
        List<Parcela> parcelaList=new ArrayList<>();
        double jurosParcela, saldoDevedor=valor;
        totalJuros = 0;
        for(int i=1; i<=prazo; i++){
            jurosParcela=saldoDevedor*juros/100;
            saldoDevedor-=parcela-jurosParcela;
            totalJuros = totalJuros + jurosParcela;
            Parcela p = new Parcela(i,parcela,jurosParcela,parcela-jurosParcela,saldoDevedor);
            parcelaList.add(p);
        }

        ParcelaAdapter parcelaAdapter=new ParcelaAdapter(this,
        R.layout.item_layout, parcelaList);
        listView.setAdapter(parcelaAdapter);
    }
    private void gerarPlanilhaSacre() {
        List<Parcela> parcelaList=new ArrayList<>();
        double jurosParcela, saldoDevedor=valor, saldoAnterior = 0;
        int flag = 0;
        totalJuros = 0;
        // primeira parcela
        amort = saldoDevedor/prazo;
        jurosParcela = (juros*saldoDevedor)/100;
        parcelaSacre = amort + jurosParcela;
        saldoDevedor = saldoDevedor - amort;
        totalJuros = totalJuros + jurosParcela;
        Parcela parcela1 = new Parcela(1,parcelaSacre,jurosParcela,amort,saldoDevedor);
        parcelaList.add(parcela1);
        for(int i=2; i<=prazo; i++)
        {

            if((i-1)%12 == 0) // reajuste
            {

                amort = saldoDevedor/(prazo - i + 1);
                jurosParcela = (juros*saldoDevedor)/100;
                parcelaSacre = jurosParcela + amort;
                saldoDevedor = saldoDevedor - amort;
                if(saldoDevedor > 0)
                {
                    Parcela p = new Parcela(i,parcelaSacre,jurosParcela,amort,saldoDevedor);
                    parcelaList.add(p);
                }
                else
                {
                    Parcela p = new Parcela(i,0,0,0,0);
                    parcelaList.add(p);
                }

            }
            else
            {
                jurosParcela = (juros*saldoDevedor)/100;
                amort = parcelaSacre-jurosParcela;
                saldoDevedor = saldoDevedor - amort;
                if(saldoDevedor > 0)
                {
                    saldoAnterior = saldoDevedor;
                    Parcela p = new Parcela(i,parcelaSacre,jurosParcela,amort,saldoDevedor);
                    parcelaList.add(p);
                }
                else
                {
                    if (flag == 0) // ultima parcela
                    {
                        parcelaSacre = saldoAnterior + jurosParcela;
                        amort = saldoAnterior;
                        saldoDevedor = 0;
                        Parcela p = new Parcela(i,parcelaSacre,jurosParcela,amort,saldoDevedor);
                        parcelaList.add(p);
                        flag = 1;
                        parcelaSacre = 0;
                        totalJuros = totalJuros + jurosParcela;
                        jurosParcela = 0;
                        amort = 0;
                    }
                    else
                    {
                        Parcela p = new Parcela(i,parcelaSacre,jurosParcela,amort,saldoDevedor);
                        parcelaList.add(p);
                    }

                }

            }
            totalJuros = totalJuros + jurosParcela;
        }
        ParcelaAdapter parcelaAdapter=new ParcelaAdapter(this,
        R.layout.item_layout, parcelaList);
        listView.setAdapter(parcelaAdapter);

    }

}