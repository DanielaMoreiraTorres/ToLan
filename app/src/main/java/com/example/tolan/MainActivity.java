package com.example.tolan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.MenuItem;

import com.example.tolan.clases.ClssConvertirTextoAVoz;
import com.example.tolan.fragments.FrgLogin;
import com.example.tolan.fragments.FrgRegisterUser;
import com.example.tolan.fragments.FrgWelcome;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Fragment fragment;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    //static ClssConvertirTextoAVoz tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().hide();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        //tts = new ClssConvertirTextoAVoz();
        //tts.init(this);

        fragment = new FrgWelcome();
        getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar,menu);
        MenuItem mc = menu.findItem(R.id.btnCaritas);
        mc.setVisible(false);
        MenuItem mr = menu.findItem(R.id.btnRecompensa);
        mr.setVisible(false);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.btnCaritas) {
            //tts.reproduce("Tienes ");
        }
        /*if(id == R.id.btnMyInfo) {
            tts.reproduce("Mi información");
            fragment = new FrgRegisterUser();
            getSupportFragmentManager().popBackStack();
            getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).addToBackStack(null).commit();
        }*/
        if(id == R.id.btnLogIn) {
            //tts.reproduce("Cerrar sesión");
            ClssConvertirTextoAVoz.getIntancia(this).reproduce("Cerrar sesión");
            fragment = new FrgLogin();
            getSupportFragmentManager().popBackStack();
            getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).addToBackStack(null).commit();
        }
        if(id == R.id.btnContacts) {
            //tts.reproduce("Información de contacto");
            ClssConvertirTextoAVoz.getIntancia(this).reproduce("Información de contacto");
            Intent intent = new Intent(MainActivity.this, ActivityContact.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}