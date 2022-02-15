package com.example.tolan.adapters;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.tolan.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class VHoldRecyclerMain_SectionSubnivel extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView sectionNameTextView, txt_numeroNivel;
    CircleImageView imageView_section;
    RecyclerView childRecyclerView;

    public VHoldRecyclerMain_SectionSubnivel(@NonNull View itemView) {
        super(itemView);

        sectionNameTextView = itemView.findViewById(R.id.sectionNameTextView);
        imageView_section = itemView.findViewById(R.id.imageView_section);
        imageView_section.setOnClickListener(this);
        childRecyclerView = itemView.findViewById(R.id.childRecyclerView);
        txt_numeroNivel = itemView.findViewById(R.id.txt_numeroNivel);
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(v.getContext(),"Sección "+sectionNameTextView.getText(),Toast.LENGTH_SHORT).show();

    }
}
