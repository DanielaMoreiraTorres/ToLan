package com.example.tolan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.tolan.Adapter.AdapRecy_SubNivel;
import com.example.tolan.models.subnivel;

import java.util.ArrayList;
import java.util.List;

public class Subnivel extends AppCompatActivity {

    Toolbar toolbar;
    private RecyclerView recView;
    private AdapRecy_SubNivel adapter;
    private List<subnivel> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subnivel);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        recView = findViewById(R.id.listSubnivel);
        recView.setLayoutManager(new LinearLayoutManager(this));

        /*items = getItems();

        adapter= new AdapRecy_SubNivel(items);
        recView.setAdapter(adapter);*/


        //initView();
        //InitValues();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.btnNotifi) {

        }
        if(id == R.id.btnLogIn) {
            Intent intent = new Intent(this, ActivityLogin.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
        if(id == R.id.btnContacts) {
            Intent intent = new Intent(this, ActivityContacts.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    /*
    private void initView(){
        recView=findViewById(R.id.listSubnivel);
    }

    private void InitValues(){
        LinearLayoutManager linear= new LinearLayoutManager(this);
        recView.setLayoutManager(linear);

        items = getItems();
        adapter = new AdapRecy_SubNivel(items);
        recView.setAdapter(adapter);
    }

    private List<subnivel> getItems(){
        List<subnivel> itemsLists = new ArrayList<>();
        itemsLists.add(new subnivel(1,"Nivel Basico","Sin contexto",4,true));
        itemsLists.add(new subnivel(2,"Nivel Medio","Sin contexto",3,true));
        itemsLists.add(new subnivel(3,"Nivel Alto","Sin contexto",2,true));
        return itemsLists;
    }
     */
}