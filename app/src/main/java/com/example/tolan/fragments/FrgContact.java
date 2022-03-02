package com.example.tolan.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.provider.Browser;
import android.text.Html;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.codesgood.views.JustifiedTextView;
import com.example.tolan.R;
import com.example.tolan.adapters.AdpContact;
import com.example.tolan.clases.ClssConvertirTextoAVoz;
import com.example.tolan.models.ModelContact;

import java.util.ArrayList;
import java.util.List;

public class FrgContact extends Fragment {

    private Fragment fragment;
    private RecyclerView rcvContacts, rcvD, rcvA, rcvC;
    private JustifiedTextView txtInf;
    private TextView textviewDT, textviewD, textviewA, textviewC, txtIn,link, linkfb;
    private Toolbar toolbar;

    public FrgContact() {
        // Required empty public constructor
    }

    public static FrgContact newInstance(String param1, String param2) {
        FrgContact fragment = new FrgContact();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        try {
            toolbar = view.findViewById(R.id.toolbar);
            setHasOptionsMenu(true);
            ((AppCompatActivity) this.getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity) this.getActivity()).getSupportActionBar().setTitle("");
            txtInf = view.findViewById(R.id.txtInf);
            txtInf.setOnClickListener(v-> ClssConvertirTextoAVoz.clssConvertirTextoAVoz.reproduce(txtInf.getText().toString().trim()));
            textviewDT = view.findViewById(R.id.textviewDT);
            textviewDT.setOnClickListener(v-> ClssConvertirTextoAVoz.clssConvertirTextoAVoz.reproduce(textviewDT.getText().toString().trim()));
            textviewD = view.findViewById(R.id.textviewD);
            textviewD.setOnClickListener(v-> ClssConvertirTextoAVoz.clssConvertirTextoAVoz.reproduce(textviewD.getText().toString().trim()));
            textviewA = view.findViewById(R.id.textviewA);
            textviewA.setOnClickListener(v-> ClssConvertirTextoAVoz.clssConvertirTextoAVoz.reproduce(textviewA.getText().toString().trim()));
            textviewC = view.findViewById(R.id.textviewC);
            textviewC.setOnClickListener(v-> ClssConvertirTextoAVoz.clssConvertirTextoAVoz.reproduce(textviewC.getText().toString().trim()));
            txtIn = view.findViewById(R.id.txtIn);
            txtIn.setOnClickListener(v-> ClssConvertirTextoAVoz.clssConvertirTextoAVoz.reproduce(txtIn.getText().toString().trim()));
            //Desarrollado por
            rcvContacts = view.findViewById(R.id.rcvContacts);
            LinearLayoutManager linear = new LinearLayoutManager(getContext());
            linear.setOrientation(LinearLayoutManager.VERTICAL);
            linear = new LinearLayoutManager(getContext()) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
            rcvContacts.setLayoutManager(linear);
            rcvContacts.hasFixedSize();
            AdpContact.showShimmer = true;
            List<ModelContact> list = new ArrayList<ModelContact>();
            AdpContact adpContact = new AdpContact(list);
            rcvContacts.setAdapter(adpContact);
            ModelContact c1 = new ModelContact("Bryan Zambrano Aroca");
            ModelContact c2 = new ModelContact("Cristhian Burbano Parraga");
            ModelContact c3 = new ModelContact("Daniela Moreira Torres");
            ModelContact c4 = new ModelContact("Ruben Jaya Puruncaja");
            final List<ModelContact> finalList = new ArrayList<ModelContact>();
            finalList.add(c1);
            finalList.add(c2);
            finalList.add(c3);
            finalList.add(c4);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    AdpContact adpContacts = new AdpContact(finalList);
                    rcvContacts.setAdapter(adpContacts);
                    adpContacts.showShimmer = false;
                    adpContacts.notifyDataSetChanged();
                    adpContacts.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int posicion = rcvContacts.getChildAdapterPosition(view);
                            ModelContact opSelected = finalList.get(posicion);
                            ClssConvertirTextoAVoz.clssConvertirTextoAVoz.reproduce(opSelected.getName());
                        }
                    });
                }
            }, 600);

            //Dirigido por
            rcvD = view.findViewById(R.id.rcvD);
            LinearLayoutManager linearD = new LinearLayoutManager(getContext());
            linearD.setOrientation(LinearLayoutManager.VERTICAL);
            linearD = new LinearLayoutManager(getContext()) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
            rcvD.setLayoutManager(linearD);
            rcvD.hasFixedSize();
            AdpContact.showShimmer = true;
            List<ModelContact> listD = new ArrayList<ModelContact>();
            AdpContact adpContactD = new AdpContact(listD);
            rcvD.setAdapter(adpContactD);
            ModelContact d1 = new ModelContact("Orlando Erazo Moreta");
            ModelContact d2 = new ModelContact("Mercedes Moreira Menendez");
            final List<ModelContact> finalListD = new ArrayList<ModelContact>();
            finalListD.add(d1);
            finalListD.add(d2);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    AdpContact adpContactsD = new AdpContact(finalListD);
                    rcvD.setAdapter(adpContactsD);
                    adpContactsD.showShimmer = false;
                    adpContactsD.notifyDataSetChanged();
                    adpContactsD.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int posicion = rcvD.getChildAdapterPosition(view);
                            ModelContact opSelected = finalListD.get(posicion);
                            ClssConvertirTextoAVoz.clssConvertirTextoAVoz.reproduce(opSelected.getName());
                        }
                    });
                }
            }, 600);

            //En la administración de
            rcvA = view.findViewById(R.id.rcvA);
            LinearLayoutManager linearA = new LinearLayoutManager(getContext());
            linearA.setOrientation(LinearLayoutManager.VERTICAL);
            linearA = new LinearLayoutManager(getContext()) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
            rcvA.setLayoutManager(linearA);
            rcvA.hasFixedSize();
            AdpContact.showShimmer = true;
            List<ModelContact> listA = new ArrayList<ModelContact>();
            AdpContact adpContactA = new AdpContact(listA);
            rcvA.setAdapter(adpContactA);
            ModelContact a1 = new ModelContact("Eduardo Díaz Ocampo (Rector)");
            ModelContact a2 = new ModelContact("Jenny Torres (Vicerrectora Académica)");
            ModelContact a3 = new ModelContact("Roberto Pico (Vicerrector Administrativo)");
            ModelContact a4 = new ModelContact("Henry Alarcón (Director de Vinculación)");
            ModelContact a5 = new ModelContact("Washington Chiriboga (Decano)");
            ModelContact a6 = new ModelContact("Stalin Carreño (Unidad de TIC)");
            final List<ModelContact> finalListA = new ArrayList<ModelContact>();
            finalListA.add(a1);
            finalListA.add(a2);
            finalListA.add(a3);
            finalListA.add(a4);
            finalListA.add(a5);
            finalListA.add(a6);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    AdpContact adpContactsA = new AdpContact(finalListA);
                    rcvA.setAdapter(adpContactsA);
                    adpContactsA.showShimmer = false;
                    adpContactsA.notifyDataSetChanged();
                    adpContactsA.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int posicion = rcvA.getChildAdapterPosition(view);
                            ModelContact opSelected = finalListA.get(posicion);
                            ClssConvertirTextoAVoz.clssConvertirTextoAVoz.reproduce(opSelected.getName());
                        }
                    });
                }
            }, 600);

            //Con la colaboración de
            rcvC = view.findViewById(R.id.rcvC);
            LinearLayoutManager linearC = new LinearLayoutManager(getContext());
            linearC.setOrientation(LinearLayoutManager.VERTICAL);
            linearC = new LinearLayoutManager(getContext()) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
            rcvC.setLayoutManager(linearC);
            rcvC.hasFixedSize();
            AdpContact.showShimmer = true;
            List<ModelContact> listC = new ArrayList<ModelContact>();
            AdpContact adpContactC = new AdpContact(listC);
            rcvC.setAdapter(adpContactC);
            ModelContact cc1 = new ModelContact("Félix Mesa Vélez");
            final List<ModelContact> finalListC = new ArrayList<ModelContact>();
            finalListC.add(cc1);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    AdpContact adpContactsC = new AdpContact(finalListC);
                    rcvC.setAdapter(adpContactsC);
                    adpContactsC.showShimmer = false;
                    adpContactsC.notifyDataSetChanged();
                    adpContactsC.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int posicion = rcvC.getChildAdapterPosition(view);
                            ModelContact opSelected = finalListC.get(posicion);
                            ClssConvertirTextoAVoz.clssConvertirTextoAVoz.reproduce(opSelected.getName());
                        }
                    });
                }
            }, 600);

            link = view.findViewById(R.id.link);
            linkfb = view.findViewById(R.id.linkfb);
            Linkify.addLinks(link,Linkify.PHONE_NUMBERS | Linkify.WEB_URLS);
            Linkify.addLinks(linkfb,Linkify.PHONE_NUMBERS | Linkify.WEB_URLS);
            link.setOnClickListener(v -> goLink(v));
            linkfb.setOnClickListener(v -> goLink(v));
        }
        catch (Exception e) {}
        return view;
    }

    private void goLink(View v){
        try {
            TextView textView = (TextView) v;
            Uri uri = Uri.parse(textView.getText().toString().trim());
            Context context = v.getContext();
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName());
            context.startActivity(intent);
        } catch (Exception e) {}
    }
}