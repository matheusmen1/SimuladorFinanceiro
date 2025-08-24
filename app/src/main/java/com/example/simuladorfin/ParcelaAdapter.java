package com.example.simuladorfin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ParcelaAdapter extends ArrayAdapter<Parcela> {
    private int resource;
    public ParcelaAdapter(@NonNull Context context, int resource, @NonNull List<Parcela> parcelas) {
        super(context, resource, parcelas);
        this.resource=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater)
                    getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(this.resource,parent,false);
        }
        TextView tvNumero=convertView.findViewById(R.id.tvNumero);
        TextView tvParcela2=convertView.findViewById(R.id.tvParcela2);
        TextView tvSaldo=convertView.findViewById(R.id.tvSaldo);
        tvNumero.setText(""+getItem(position).getNum());
        tvParcela2.setText(String.format("%.2f",getItem(position).getValor()));
        tvSaldo.setText(String.format("%.2f",getItem(position).getSaldo()));
        return convertView;
    }
}
