package com.example.tolan.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tolan.R;
import com.example.tolan.models.ModelUser;

import java.util.ArrayList;
import java.util.List;

public final class AdpAutocompleteDocente extends ArrayAdapter<ModelUser> {

    private List<ModelUser> listaFull;

    public AdpAutocompleteDocente(@NonNull Context context, @NonNull List<ModelUser> lista) {
        super(context, 0, lista);
        listaFull = new ArrayList<>(lista);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return listaFilter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_suggestion_docente, parent, false);
        }
        TextView txtnom = (TextView) convertView.findViewById(R.id.nameDocente);

        ModelUser user = getItem(position);
        if(user != null){
            txtnom.setText(user.getNombres() + " " + user.getApellidos());
        }
        return convertView;
    }

    private Filter listaFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults results = new FilterResults();
            List<ModelUser> suggestions = new ArrayList<>();
            if(charSequence == null || charSequence.length() == 0){
                suggestions.addAll(listaFull);
            }
            else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for(ModelUser user : listaFull){
                    if(user.getNombres().toLowerCase().contains(filterPattern) || user.getApellidos().toLowerCase().contains(filterPattern))
                        suggestions.add(user);
                }
            }
            results.values = suggestions;
            results.count = suggestions.size();
            return  results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            clear();
            addAll((List) filterResults.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return (((ModelUser) resultValue).getNombres() + " " + ((ModelUser) resultValue).getApellidos());
        }
    };
}
