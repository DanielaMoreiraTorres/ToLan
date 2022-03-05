package com.example.tolan.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.tolan.R;
import com.example.tolan.clases.ClssConvertTextToSpeech;

import de.hdodenhof.circleimageview.CircleImageView;

public class VHoldRecyclerMain_SectionSubnivel extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView sectionNameTextView, txt_numeroNivel;
    CircleImageView imageView_section;
    RecyclerView childRecyclerView;
    LinearLayout ly_continueActivities;
    ImageView imgNivel;
    //ClssConvertTextToSpeech clssConvertirTextoAVoz;

    public VHoldRecyclerMain_SectionSubnivel(@NonNull View itemView) {
        super(itemView);

        sectionNameTextView = itemView.findViewById(R.id.sectionNameTextView);
        imageView_section = itemView.findViewById(R.id.imageView_section);
        imageView_section.setOnClickListener(this);
        childRecyclerView = itemView.findViewById(R.id.childRecyclerView);
        txt_numeroNivel = itemView.findViewById(R.id.txt_numeroNivel);
        ly_continueActivities = itemView.findViewById(R.id.ly_continueActivities);
        imgNivel = itemView.findViewById(R.id.imgNivel);
        //clssConvertirTextoAVoz = new ClssConvertTextToSpeech();
        //clssConvertirTextoAVoz.init(itemView.getContext());

    }

    @Override
    public void onClick(View v) {
        Toast.makeText(v.getContext(), "Sección " + sectionNameTextView.getText(), Toast.LENGTH_SHORT).show();
        //clssConvertirTextoAVoz.reproduce("Sección " + sectionNameTextView.getText());
        ClssConvertTextToSpeech.getIntancia(v.getContext()).reproduce("Sección " + sectionNameTextView.getText());
    }
}
