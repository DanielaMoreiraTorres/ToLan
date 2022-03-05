package com.example.tolan.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.tolan.R;
import com.example.tolan.models.ModelContent;

import java.util.List;

public class AdpStatement extends ArrayAdapter<ModelContent> {

    //constructor
    public AdpStatement(Context context, List<ModelContent> datos) {
        super(context, R.layout.item_enunciado, datos);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        //Funciona como DataBinding
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View item = inflater.inflate(R.layout.item_enunciado, null);
        TextView lblTitulo = (TextView)item.findViewById(R.id.lblTitulo);
        lblTitulo.setText(getItem(position).getDescripcion());
        return(item);
    }
}
