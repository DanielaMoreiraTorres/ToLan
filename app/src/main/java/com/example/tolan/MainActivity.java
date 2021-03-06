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
import android.view.View;
import android.widget.ProgressBar;

import com.example.tolan.adapters.VHoldRecyclerChild_ItemSubnivel;
import com.example.tolan.clases.ClssConvertTextToSpeech;
import com.example.tolan.clases.ClssPreferences;
import com.example.tolan.clases.ClssStaticUser;
import com.example.tolan.controller.ControllerUser;
import com.example.tolan.fragments.FrgContact;
import com.example.tolan.fragments.FrgLogin;
import com.example.tolan.fragments.FrgMenuAdmin;
import com.example.tolan.fragments.FrgMenuTeacher;
import com.example.tolan.fragments.FrgRegisterUser;
import com.example.tolan.fragments.FrgWelcome;
import com.example.tolan.models.ModelUser;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Fragment fragment;
    private ControllerUser controllerUser;
    ProgressBar progressBar;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private int aux = 0, auxInfo = 0;
    private String usuario = "", clave = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            progressBar = findViewById(R.id.progressBar);
            controllerUser = new ControllerUser(this, progressBar);
            usuario = ClssPreferences.getIntancia(this).leerValor("user");
            clave = ClssPreferences.getIntancia(this).leerValor("password");
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
            //tts = new ClssConvertTextToSpeech()
            //tts.init(this);
            if (usuario.length() > 0 & clave.length() > 0) {
                progressBar.setVisibility(View.VISIBLE);
                controllerUser.Login(usuario, clave);
            } else {
                fragment = new FrgWelcome();
                getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
            }
        } catch (Exception e) {}
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.btnCaritas) {
            ClssConvertTextToSpeech.getIntancia(this).reproduce("Tienes "+ ModelUser.stockcaritas + " recompensas ganadas");
            if(aux != 0)
                aux = 0;
            if (auxInfo != 0)
                auxInfo = 0;
        }
        if(id == R.id.btnMyInfo) {
            ClssConvertTextToSpeech.getIntancia(this).reproduce("Mi informaci??n");
            if(auxInfo == 0) {
                fragment = new FrgRegisterUser();
                auxInfo += 1;
                getSupportFragmentManager().popBackStack();
                Bundle bundle = new Bundle();
                //A??admimos al bundle la lista que pasaremos como parametro
                bundle.putBoolean("editar", true);
                fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
            }

            /*if(auxInfo == 0) {

            }
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
            ClssConvertTextToSpeech.getIntancia(this).reproduce("Cerrar sesi??n");
            ClssPreferences.getIntancia(this).resetValor();
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
            ClssConvertTextToSpeech.getIntancia(this).reproduce("Acerca de");
            if(aux == 0) {
                fragment = new FrgContact();
                aux += 1;
                getSupportFragmentManager().popBackStack();
                getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
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
                if (i > 2) {
                    if (aux != 0)
                        aux = 0;
                    if (auxInfo != 0)
                        auxInfo = 0;
                    super.onBackPressed();
                }
                else if (i == 2) {
                    fragment = new FrgLogin();
                    getSupportFragmentManager().popBackStack();
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).addToBackStack(null).commit();
                } else if (i == 1) {
                    fragment = new FrgWelcome();
                    getSupportFragmentManager().popBackStack();
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
                } else if (i == 0) {
                    if (aux != 0 || auxInfo != 0) {
                        if(aux != 0) aux = 0;
                        if(auxInfo != 0) auxInfo = 0;
                        if(ClssStaticUser.tipousuario.equals("AD")) {
                            fragment = new FrgMenuAdmin();
                            getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).addToBackStack(null).commit();
                        } else if(ClssStaticUser.tipousuario.equals("DC")) {
                            fragment = new FrgMenuTeacher();
                            getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).addToBackStack(null).commit();
                        } else if(ClssStaticUser.tipousuario.equals("ES")) {
                            fragment = new ActivityHomeUser();
                            getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).addToBackStack(null).commit();
                        }
                    }
                    /*if (auxInfo != 0) {
                        auxInfo = 0;
                        if(ClssStaticUser.tipousuario.equals("AD")) {
                            fragment = new FrgMenuAdmin();
                            getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).addToBackStack(null).commit();
                        } else if(ClssStaticUser.tipousuario.equals("DC")) {
                            fragment = new FrgMenuTeacher();
                            getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).addToBackStack(null).commit();
                        } else if(ClssStaticUser.tipousuario.equals("ES")) {
                            fragment = new ActivityHomeUser();
                            getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).addToBackStack(null).commit();
                        }
                    }*/
                    else super.onBackPressed();
                }
            } else {
                @SuppressLint("RestrictedApi")
                int nav = c.getBackStack().size();
                if(nav > 2){
                    if(i == 0) {
                        if (aux != 0 || auxInfo != 0) {
                            if(aux != 0) aux = 0;
                            if(auxInfo != 0) auxInfo = 0;
                            if(ClssStaticUser.tipousuario.equals("AD")) {
                                fragment = new FrgMenuAdmin();
                                getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).addToBackStack(null).commit();
                            } else if(ClssStaticUser.tipousuario.equals("DC")) {
                                fragment = new FrgMenuTeacher();
                                getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).addToBackStack(null).commit();
                            } else if(ClssStaticUser.tipousuario.equals("ES")) {
                                fragment = new ActivityHomeUser();
                                getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).addToBackStack(null).commit();
                            }
                        }
                    } else c.popBackStack(R.id.inicioFragment, false);
                }
                else if (nav == 2){
                    if(i > 1) {
                        fragment = new FrgLogin();
                        getSupportFragmentManager().popBackStack();
                        getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).addToBackStack(null).commit();
                    } else if(i == 1){
                        fragment = new FrgWelcome();
                        getSupportFragmentManager().popBackStack();
                        getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
                    }
                    else super.onBackPressed();
                /*} else if (nav == 1) {
                    if(i != 0) {
                        fragment = new FrgWelcome();
                        getSupportFragmentManager().popBackStack();
                        getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
                    }
                    else super.onBackPressed();*/
                } else if(nav == 0){
                    if(i == 0) {
                        if (aux != 0 || auxInfo != 0) {
                            if(aux != 0) aux = 0;
                            if(auxInfo != 0) auxInfo = 0;
                            if(ClssStaticUser.tipousuario.equals("AD")) {
                                fragment = new FrgMenuAdmin();
                                getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).addToBackStack(null).commit();
                            } else if(ClssStaticUser.tipousuario.equals("DC")) {
                                fragment = new FrgMenuTeacher();
                                getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).addToBackStack(null).commit();
                            } else if(ClssStaticUser.tipousuario.equals("ES")) {
                                fragment = new ActivityHomeUser();
                                getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).addToBackStack(null).commit();
                            }
                        }
                    }
                    else super.onBackPressed();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}