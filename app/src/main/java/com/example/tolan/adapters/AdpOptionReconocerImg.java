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

public class AdpOptionReconocerImg extends ArrayAdapter<ModelContent> {

    //constructor
    public AdpOptionReconocerImg(Context context, List<ModelContent> datos) {
        super(context, R.layout.item_option_reconocer_figura, datos);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        //Funciona como DataBinding
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View item = inflater.inflate(R.layout.item_option_reconocer_figura, null);
        TextView lblOp = (TextView)item.findViewById(R.id.lblOp);
        lblOp.setText("Opción " + (position + 1) + ".- ");
        TextView txtOp = (TextView)item.findViewById(R.id.txtOp);
        txtOp.setText(getItem(position).getDescripcion());
        return(item);
    }
}
