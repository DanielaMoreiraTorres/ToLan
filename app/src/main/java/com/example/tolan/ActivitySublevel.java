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

import com.example.tolan.adapters.AdpSubnivel;
import com.example.tolan.models.ModelSubnivel;

import java.util.List;

public class ActivitySublevel extends AppCompatActivity {

    Toolbar toolbar;
    private RecyclerView recView;
    private AdpSubnivel adapter;
    private List<ModelSubnivel> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sublevel);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        recView = findViewById(R.id.listSubnivel);
        recView.setLayoutManager(new LinearLayoutManager(this));

        /*items = getItems();

        adapter= new AdpSubnivel(items);
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
            Intent intent = new Intent(this, ActivityContact.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
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
        adapter = new AdpSubnivel(items);
        recView.setAdapter(adapter);
    }

    private List<ModelSubnivel> getItems(){
        List<ModelSubnivel> itemsLists = new ArrayList<>();
        itemsLists.add(new ModelSubnivel(1,"Nivel Basico","Sin contexto",4,true));
        itemsLists.add(new ModelSubnivel(2,"Nivel Medio","Sin contexto",3,true));
        itemsLists.add(new ModelSubnivel(3,"Nivel Alto","Sin contexto",2,true));
        return itemsLists;
    }
     */
}