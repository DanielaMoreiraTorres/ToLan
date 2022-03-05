package com.example.tolan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.tolan.adapters.VHoldRecyclerChild_ItemSubnivel;
import com.example.tolan.clases.ClssConvertTextToSpeech;
import com.example.tolan.fragments.FrgContact;
import com.example.tolan.fragments.FrgLogin;
import com.example.tolan.fragments.FrgWelcome;
import com.example.tolan.models.ModelUser;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Fragment fragment;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private int aux = 0, auxInfo = 0;
    //static ClssConvertTextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ArrarList de permisos
        ArrayList<String> permisos = new ArrayList<String>();
        permisos.add(Manifest.permission.INTERNET);
        permisos.add(Manifest.permission.CAMERA);
        permisos.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permisos.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        getPermission(permisos);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().hide();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        //tts = new ClssConvertTextToSpeech();
        //tts.init(this);

        fragment = new FrgWelcome();
        getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
    }

    private void getPermission(ArrayList<String> permisosSolicitados)
    {
        ArrayList<String> listPermisosNOAprob = getPermisosNoAprobados(permisosSolicitados);
        if (listPermisosNOAprob.size()>0)
            if (Build.VERSION.SDK_INT >= 23)
                requestPermissions(listPermisosNOAprob.toArray(new String[listPermisosNOAprob.size()]), 1);
    }

    private ArrayList<String> getPermisosNoAprobados(ArrayList<String> listaPermisos)
    {
        ArrayList<String> list = new ArrayList<String>();
        for(String permiso: listaPermisos) {
            if (Build.VERSION.SDK_INT >= 23)
                if(checkSelfPermission(permiso) != PackageManager.PERMISSION_GRANTED)
                    list.add(permiso);

        }
        return list;
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
            ClssConvertTextToSpeech.getIntancia(this).reproduce("Tienes "+ ModelUser.stockcaritas + " caritas ganadas");
            if(aux != 0)
                aux = 0;
            if (auxInfo != 0)
                auxInfo = 0;
        }
        if(id == R.id.btnMyInfo) {
            /*ClssConvertTextToSpeech.getIntancia(this).reproduce("Mi información");
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
            ClssConvertTextToSpeech.getIntancia(this).reproduce("Cerrar sesión");
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
            ClssConvertTextToSpeech.getIntancia(this).reproduce("Información de contacto");
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
            ClssConvertTextToSpeech.getIntancia(this).reproduce("Regresar");
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