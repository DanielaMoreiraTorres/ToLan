package com.example.tolan.fragments;

import static android.content.ClipData.newPlainText;

import android.content.ClipData;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tolan.R;
import com.example.tolan.clases.ClssNavegacionActividades;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FrgGrafomotricity#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FrgGrafomotricity extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FrgGrafomotricity() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FrgGrafomotricity.
     */
    // TODO: Rename and change types and number of parameters
    public static FrgGrafomotricity newInstance(String param1, String param2) {
        FrgGrafomotricity fragment = new FrgGrafomotricity();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_grafomotricity, container, false);
    }

    JSONArray jsonActivities;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            String lst_Activities = getArguments().getString("activities");
            jsonActivities = new JSONArray(lst_Activities);
            Toast.makeText(view.getContext(), lst_Activities, Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        view.findViewById(R.id.btn_comprobar_actividades).setOnClickListener(this);
    }
    ArrayList<Bitmap> pieces;

    void Llenar(View view) {
        try {
            CardView c1 = view.findViewById(R.id.car1);
            ImageView pz1 = view.findViewById(R.id.imP1);
            ImageView pz2 = view.findViewById(R.id.imP2);
            ImageView pz3 = view.findViewById(R.id.imP3);
            ImageView pz4 = view.findViewById(R.id.imP4);
            ImageView tr1 = view.findViewById(R.id.prt1);
            ImageView tr2 = view.findViewById(R.id.prt2);
            ImageView tr3 = view.findViewById(R.id.prt3);
            ImageView tr4 = view.findViewById(R.id.prt4);
            pieces = splitImage(view);
            pz1.setImageBitmap(pieces.get(0));
            pz2.setImageBitmap(pieces.get(1));
            pz3.setImageBitmap(pieces.get(2));
            pz4.setImageBitmap(pieces.get(3));
            tr1.setImageBitmap(pieces.get(0));
            tr2.setImageBitmap(pieces.get(1));
            tr3.setImageBitmap(pieces.get(2));
            tr4.setImageBitmap(pieces.get(3));
            pz1.setOnLongClickListener(v -> LongClickListener(v));
            pz2.setOnLongClickListener(v -> LongClickListener(v));
            pz3.setOnLongClickListener(v -> LongClickListener(v));
            pz4.setOnLongClickListener(v -> LongClickListener(v));
            tr1.setOnDragListener((v, event) -> DragListener(v, event));
            tr2.setOnDragListener((v, event) -> DragListener(v, event));
            tr3.setOnDragListener((v, event) -> DragListener(v, event));
            tr4.setOnDragListener((v, event) -> DragListener(v, event));
        } catch (Exception e) {
        }
    }

    String respuesta="";
    private boolean LongClickListener(View v) {
        try {
            ClipData data = newPlainText((CharSequence) v.getTag(), "");
            // DragShadowBuilder myShadow = new DragShadowBuilder(v);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //v.startDragAndDrop(data, myShadow, null, 0);

                v.startDragAndDrop(data, new View.DragShadowBuilder(v){
                    @Override
                    public void onDrawShadow(Canvas canvas) {v.draw(canvas); }

                    @Override
                    public void onProvideShadowMetrics(Point outShadowSize, Point outShadowTouchPoint) {
                        outShadowSize.set(v.getWidth(), v.getHeight());
                        outShadowTouchPoint.set(v.getWidth() / 2, v.getHeight() / 2);
                    }
                }, null, 0);
            }
            else
                v.startDrag(data, new View.DragShadowBuilder(v){
                    @Override
                    public void onDrawShadow(Canvas canvas) {v.draw(canvas); }

                    @Override
                    public void onProvideShadowMetrics(Point outShadowSize, Point outShadowTouchPoint) {
                        outShadowSize.set(v.getWidth(), v.getHeight());
                        outShadowTouchPoint.set(v.getWidth() / 2, v.getHeight() / 2);
                    }
                }, null, 0);
        } catch (Exception e) {}
        return true;
    }
    public boolean DragListener(View view, @NonNull DragEvent dragEvent) {
        switch (dragEvent.getAction()) {
            case DragEvent.ACTION_DRAG_ENDED:
                break;
            case DragEvent.ACTION_DRAG_STARTED:
                break;
            case DragEvent.ACTION_DRAG_LOCATION:
                break;
            case DragEvent.ACTION_DROP:
                if(view.getTag().toString().equals(dragEvent.getClipDescription().getLabel())) {
                    TextView n = view.findViewById(R.id.txtMenu);
                    view.setBackgroundColor(Color.GREEN);
                    respuesta+="1";
                }
                else {
                    view.setBackgroundColor(Color.RED);
                }
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                view.invalidate();
                break;
            default:
                break;
        }
        return true;
    };
    private ArrayList<Bitmap> splitImage(View v) {
        int piecesNumber = 4;
        int rows = 2;
        int cols = 2;

        ImageView imageView = (ImageView) v.findViewById(R.id.imgEnunciado);
        ArrayList<Bitmap> pieces = new ArrayList<>(piecesNumber);

        // Get the bitmap of the source image
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        // Calculate the with and height of the pieces
        int pieceWidth = bitmap.getWidth()/cols;
        int pieceHeight = bitmap.getHeight()/rows;

        // Create each bitmap piece and add it to the resulting array
        int yCoord = 0;
        for (int row = 0; row < rows; row++) {
            int xCoord = 0;
            for (int col = 0; col < cols; col++) {
                pieces.add(Bitmap.createBitmap(bitmap, xCoord, yCoord, pieceWidth, pieceHeight));
                xCoord += pieceWidth;
            }
            yCoord += pieceHeight;
        }

        return pieces;
    }


    NavController navController;

    @Override
    public void onClick(View v) {

        navController = Navigation.findNavController(v);
        //Eliminamos el item por el cual nos redirecccionamos aca
        jsonActivities.remove(0);
        ClssNavegacionActividades clssNavegacionActividades= new ClssNavegacionActividades(navController,jsonActivities,v);
        clssNavegacionActividades.navegar();


    }

}