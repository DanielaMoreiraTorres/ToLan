package com.example.tolan.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tolan.R;

import java.util.List;

public class AdpVideos extends RecyclerView.Adapter<AdpVideos.ViewHolder> implements View.OnClickListener {

    List<Uri> videos;
    Context context;
    LayoutInflater layoutInflater;
    private View.OnClickListener listener;

    public AdpVideos(List<Uri> videos, Context context) {
        this.videos = videos;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_videos,parent,false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context)
                .load(videos.get(position))
                .into(holder.imgvideo);
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    @Override
    public void onClick(View view) {
        if(listener!=null)
            listener.onClick(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgvideo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgvideo = itemView.findViewById(R.id.vid);
        }
    }
}
