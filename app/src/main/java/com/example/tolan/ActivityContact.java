package com.example.tolan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.tolan.adapters.AdpContact;
import com.example.tolan.models.ModelContact;

import java.util.ArrayList;
import java.util.List;

public class ActivityContact extends AppCompatActivity {

    RecyclerView rcvContacts;
    Toolbar toolbar;
    ImageView imgToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        rcvContacts = findViewById(R.id.rcvContacts);
        LinearLayoutManager linear = new LinearLayoutManager(getApplicationContext());
        linear.setOrientation(LinearLayoutManager.VERTICAL);
        rcvContacts.setLayoutManager(linear);
        rcvContacts.hasFixedSize();

        AdpContact.showShimmer = true;
        List<ModelContact> list = new ArrayList<ModelContact>();
        AdpContact adpContact = new AdpContact(list);
        rcvContacts.setAdapter(adpContact);

        ModelContact c1 = new ModelContact("Burbano Parraga Cristhian","+593980395656","cristhian.burbano2016@uteq.edu.ec");
        ModelContact c2 = new ModelContact("Jaya Puruncaja Ruben","+593987943667","ruben.jaya2015@uteq.edu.ec");
        ModelContact c3 = new ModelContact("Moreira Torres Daniela","+593967037621","daniela.moreira2015@uteq.edu.ec");
        ModelContact c4 = new ModelContact("Zambrano Aroca Bryan","+593967667147","bryan.zambrano@uteq.edu.ec");

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
            }
        }, 1000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.btnMyInfo) {

        }
        if(id == R.id.btnLogIn) {
            /*
            Intent intent = new Intent(this, ActivityLogin.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);*/
        }
        if(id == R.id.btnContacts) {
            Intent intent = new Intent(this, ActivityContact.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}