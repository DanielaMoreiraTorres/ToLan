package com.example.tolan.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tolan.R;
import com.example.tolan.models.ModelContact;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;

public class AdpContact extends RecyclerView.Adapter<AdpContact.ViewHolder> implements View.OnClickListener {

    private List<ModelContact> data;
    private View.OnClickListener listener;
    public AdpContact(List<ModelContact> data){this.data = data;}
    public static boolean showShimmer = true;
    int cantShimmer = 4;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact,null,false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            if(showShimmer)
                holder.shimmerFrameLayout.startShimmer();
            else {
                holder.shimmerFrameLayout.stopShimmer();
                holder.shimmerFrameLayout.setShimmer(null);
                holder.txtContact.setBackground(null);
                //holder.txtEmail.setBackground(null);
                holder.add_data(data.get(position));
            }
        }
        catch (Exception e){ }
    }

    @Override
    public int getItemCount() {
        try{
            return showShimmer ? cantShimmer : data.size();
        }catch (Exception e) {return cantShimmer;}
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(view);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ShimmerFrameLayout shimmerFrameLayout;
        TextView txtContact;
        ImageView btnShimmer;
        /*TextView txtEmail;
        ImageView btnShimmer1;*/

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            shimmerFrameLayout = itemView.findViewById(R.id.shimmer);
            txtContact = (TextView) itemView.findViewById(R.id.txtContactName);
            btnShimmer = itemView.findViewById(R.id.imageViewliPart);
            /*txtEmail = (TextView) itemView.findViewById(R.id.txtEmail);
            btnShimmer1 = itemView.findViewById(R.id.imageViewliEm);*/
        }

        public void add_data(ModelContact valor) {
            txtContact.setText(valor.getName());
            //txtEmail.setText(valor.getEmail());
        }
    }
}
