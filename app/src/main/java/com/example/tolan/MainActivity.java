package com.example.tolan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.tolan.adapters.VHoldRecyclerChild_ItemSubnivel;
import com.example.tolan.clases.ClssConvertirTextoAVoz;
import com.example.tolan.clases.ClssNavegacionActividades;
import com.example.tolan.fragments.FrgContact;
import com.example.tolan.fragments.FrgLogin;
import com.example.tolan.fragments.FrgRegisterUser;
import com.example.tolan.fragments.FrgSublevel;
import com.example.tolan.fragments.FrgWelcome;
import com.example.tolan.models.ModelUser;

import org.json.JSONArray;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Fragment fragment;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private int aux = 0, auxInfo = 0;
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
            ClssConvertirTextoAVoz.getIntancia(this).reproduce("Tienes "+ ModelUser.stockcaritas + " caritas ganadas");
            if(aux != 0)
                aux = 0;
            if (auxInfo != 0)
                auxInfo = 0;
        }
        if(id == R.id.btnMyInfo) {
            /*ClssConvertirTextoAVoz.getIntancia(this).reproduce("Mi información");
            if(auxInfo == 0) {
                fragment = new FrgRegisterUser();
                auxInfo += 1;
                getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).addToBackStack(null).commit();
            }*/
            /*else if (auxInfo != 0)
                getSupportFragmentManager().popBackStack();
            else if(auxInfo == 0){
                fragment = new FrgRegisterUser();
                getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).addToBackStack(null).commit();
            }
            else if(aux != 0) {
                aux = 0;
            }*/
        }
        if(id == R.id.btnLogIn) {
            ClssConvertirTextoAVoz.getIntancia(this).reproduce("Cerrar sesión");
            fragment = new FrgLogin();
            int i = getSupportFragmentManager().getBackStackEntryCount();
            if(aux != 0)
                aux = 0;
            if (auxInfo != 0)
                auxInfo = 0;
            for(int c=1; c < i; c++){
                getSupportFragmentManager().popBackStack();
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
        }
        if(id == R.id.btnContacts) {
            ClssConvertirTextoAVoz.getIntancia(this).reproduce("Información de contacto");
            if(aux == 0) {
                fragment = new FrgContact();
                aux += 1;
                getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).addToBackStack(null).commit();
            }
            /*else if(aux != 0) {
                fragment = new FrgContact();
                getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).addToBackStack(null).commit();
            }
            else if(aux == 0) {
                fragment = new FrgContact();
                getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).addToBackStack(null).commit();
            }*/
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        try {
            ClssConvertirTextoAVoz.getIntancia(this).reproduce("Regresar");
            NavController c = VHoldRecyclerChild_ItemSubnivel.navController;
            int i = getSupportFragmentManager().getBackStackEntryCount();
            if (c == null) {
                if (i == 2) {
                    fragment = new FrgLogin();
                    getSupportFragmentManager().popBackStack();
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
                } else if (i == 1) {
                    fragment = new FrgWelcome();
                    getSupportFragmentManager().popBackStack();
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
                } else if (i == 0 || i > 2) {
                    if(aux != 0)
                        aux = 0;
                    if (auxInfo != 0)
                        auxInfo = 0;
                    super.onBackPressed();
                }
            } else {
                @SuppressLint("RestrictedApi")
                int nav = c.getBackStack().size();
                if(nav > 2){
                    c.popBackStack(R.id.inicioFragment,false);
                }
                else if (nav == 2){
                    c.popBackStack();
                    fragment = new FrgLogin();
                    getSupportFragmentManager().popBackStack();
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
                }
                else if(nav == 0){
                    if(i == 0)
                        super.onBackPressed();
                    else {
                        c = null;
                        fragment = new FrgWelcome();
                        getSupportFragmentManager().popBackStack();
                        getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}