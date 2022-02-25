package com.example.tolan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class ActivityMenuAdmin extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_admin);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        /*if(id == R.id.btnMyInfo) {

        }*/
        if(id == R.id.btnLogIn) {
            /*Intent intent = new Intent(this, ActivityLogin.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);*/
        }
        if(id == R.id.btnContacts) {
            Intent intent = new Intent(this, ActivityContact.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void OptionsMenuAdmin(View view){
        int tag = Integer.parseInt(view.getTag().toString());
        //Toast.makeText(ActivityMenuAdmin.this,String.valueOf(id),Toast.LENGTH_SHORT).show();
        if(tag == 1) {
            Intent intent = new Intent(ActivityMenuAdmin.this, ActivityLevel.class);
            startActivity(intent);
        }
        else if(tag == 2){
            Intent intent = new Intent(ActivityMenuAdmin.this, ActivitySublevel.class);
            startActivity(intent);
        }
        else if(tag == 3){

        }
        else if(tag == 4){
            Intent intent = new Intent(ActivityMenuAdmin.this, ActivitySkin.class);
            startActivity(intent);
        }
        else if(tag == 5){
            Intent intent = new Intent(ActivityMenuAdmin.this, activity_group_admin.class);
            startActivity(intent);
        }
        else if(tag == 6){

        }
    }
}