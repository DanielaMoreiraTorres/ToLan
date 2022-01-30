package com.example.tolan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.tolan.adapters.AdpVideo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ActivityChooseVideo extends AppCompatActivity {

    Toolbar toolbar;
    FloatingActionButton btnimagen;
    RecyclerView rcvVideos;
    public static final int video = 1;
    ClipData clipData;
    Uri uri;
    List<Uri> videos;
    AdpVideo adapter;
    Uri videoselected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_video);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        rcvVideos = findViewById(R.id.rcvVideos);
        videos = new ArrayList<Uri>();

        btnimagen = findViewById(R.id.seleccionarimg);
        btnimagen.setOnClickListener(v -> openGallery());
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

    private void openGallery() {
        try {
            Intent intent = new Intent();
            intent.setType("video/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Seleccione video"), video);
        }catch(Exception e){ }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == video) {
            clipData = data.getClipData();
            if(clipData != null) {
                if(clipData.getItemCount() > 10)
                    Toast.makeText(ActivityChooseVideo.this,"El límite es de 10 videos",Toast.LENGTH_LONG).show();
                else {
                    videos.clear();
                    for (int i = 0; i < clipData.getItemCount(); i = i + 1) {
                        ClipData.Item item = clipData.getItemAt(i);
                        uri = item.getUri();
                        videos.add(uri);
                    }
                    adapter = new AdpVideo(videos, this);
                    rcvVideos.setLayoutManager(new GridLayoutManager(this,2));
                    rcvVideos.setAdapter(adapter);
                    adapter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int opcselec = rcvVideos.getChildAdapterPosition(view);
                            videoselected = videos.get(opcselec);
                            videos.remove(opcselec);
                            adapter.notifyItemRemoved(opcselec);
                        }
                    });
                }
            }
            else{
                uri = data.getData();
                //asignar video
                videos.clear();
                videos.add(uri);
                adapter = new AdpVideo(videos, this);
                rcvVideos.setLayoutManager(new GridLayoutManager(this,2));
                rcvVideos.setAdapter(adapter);
                adapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int opcselec = rcvVideos.getChildAdapterPosition(view);
                        videoselected = videos.get(opcselec);
                        videos.remove(opcselec);
                        adapter.notifyItemRemoved(opcselec);
                    }
                });
            }
        }
    }
}