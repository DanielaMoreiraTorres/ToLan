package com.example.tolan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.tolan.Adapter.AdapRecy_SubNivel;
import com.example.tolan.models.subnivel;

import java.util.ArrayList;
import java.util.List;

public class Subnivel extends AppCompatActivity {

    private RecyclerView recView;
    private AdapRecy_SubNivel adapter;
    private List<subnivel> items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subnivel);
        items = getItems();
        recView=(RecyclerView)findViewById(R.id.listSubnivel);
        recView.setLayoutManager(new LinearLayoutManager(this));
        adapter= new AdapRecy_SubNivel(items);
        recView.setAdapter(adapter);


        //initView();
        //InitValues();
    }

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
}