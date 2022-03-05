package com.example.tolan.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

import com.example.tolan.clases.ClssConvertirTextoAVoz;
import com.example.tolan.clases.ClssVolleySingleton;
import com.example.tolan.models.ModelRecyclerItemNivel;
import com.example.tolan.models.ModelRecyclerItemSubnivel;
import com.example.tolan.R;
import com.example.tolan.models.ModelRecyclerItemNivel;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.List;

public class AdpRecycler_Main extends RecyclerView.Adapter<VHoldRecyclerMain_SectionSubnivel> {

    List<ModelRecyclerItemNivel> sectionList;
    private final Context mContext;

    public AdpRecycler_Main(Context mContext, List<ModelRecyclerItemNivel> sectionList) {
        this.sectionList = sectionList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public VHoldRecyclerMain_SectionSubnivel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.fragment_section_subnivel, parent, false);
        return new VHoldRecyclerMain_SectionSubnivel(view);
    }


    List<ModelRecyclerItemSubnivel> lstitem_Activities;

    @Override
    public void onBindViewHolder(@NonNull VHoldRecyclerMain_SectionSubnivel holder, int position) {

        ModelRecyclerItemNivel section = sectionList.get(position);
        String sectionName = section.getSectionName();
        String section_image_id = section.getImage();
        lstitem_Activities = section.getSectionItems();

        holder.sectionNameTextView.setText(sectionName);
        holder.txt_numeroNivel.setText("Nivel " + (position + 1));
        if (position == lstitem_Activities.size()) {
            holder.ly_continueActivities.setVisibility(View.GONE);
        }
        holder.imgNivel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Nivel [" + section.getSectionName() + "]", Toast.LENGTH_SHORT).show();
                //holder.clssConvertirTextoAVoz.reproduce("Nivel [" + section.getSectionName() + "]");
                ClssConvertirTextoAVoz.getIntancia(v.getContext()).reproduce("Nivel [" + section.getSectionName() + "]");
            }
        });

/*
        try {
            Glide.with(mContext)
                    .load(section_image_id)
                    .into(holder.imageView_section);

        } catch (Exception ex) {
            System.out.println("-------------------------------------------------------");
            System.out.println(ex.getMessage());
        }
*/
        cargarImagenWebService(section_image_id, holder);

        AdpRecycler_Child childRecyclerAdapter = new AdpRecycler_Child(mContext, lstitem_Activities);
        holder.childRecyclerView.setAdapter(childRecyclerAdapter);


        //Centrar los elementos
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(mContext);


        layoutManager.setJustifyContent(JustifyContent.SPACE_AROUND);
        layoutManager.setAlignItems(AlignItems.CENTER);
        layoutManager.setFlexDirection(FlexDirection.ROW);

        holder.childRecyclerView.setLayoutManager(layoutManager);


        //layoutManager.setMaxLine(2);


        ///Configurar activities pro y normales
        //PRO= EVALUACIONES DE UNIDAD
        //NORMALES= ACTIVIDADES DE RECREACIÓN


        //StaggeredGridLayoutManager staggeredGridLayoutManager =
        //       new StaggeredGridLayoutManager(2, LinearLayoutManager.HORIZONTAL);
        //holder.childRecyclerView.setLayoutManager(staggeredGridLayoutManager);


/*
        GridLayoutManager glm = new GridLayoutManager(mContext, 2);
        glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            
            @Override
            public int getSpanSize(int position) {
                if (position % 3 == 0) {
                    return 2;
                } else {
                    return 1;
                }
            }
            
        });
        //holder.childRecyclerView.setLayoutManager(new GridLayoutManager(mContext,2));
        holder.childRecyclerView.setLayoutManager(glm);
        */


    }


    private void cargarImagenWebService(String rutaImagen, final VHoldRecyclerMain_SectionSubnivel holder) {
        ImageRequest imageRequest = new ImageRequest(rutaImagen, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                holder.imageView_section.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof NetworkError) {
                    Toast.makeText(mContext,
                            "Oops. Network Error! " + error.toString(),
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(mContext,
                            "Oops. Server Error! " + error.toString(),
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(mContext,
                            "Oops. Auth FailureError! " + error.toString(),
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(mContext,
                            "Oops. Parse Error! " + error.toString(),
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof NoConnectionError) {
                    Toast.makeText(mContext,
                            "Oops. NoConnection Error! " + error.toString(),
                            Toast.LENGTH_LONG).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(mContext,
                            "Oops. Timeout error! " + error.toString(),
                            Toast.LENGTH_LONG).show();
                } else {

                    //Toast.makeText(mContext, "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
                    System.out.println("Error al cargar la imagen");
                }
                //
            }
        });
        //request.add(imageRequest);
        ClssVolleySingleton.getIntanciaVolley(mContext).addToRequestQueue(imageRequest);
    }


    @Override
    public int getItemCount() {
        return sectionList.size();
    }


}
