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
import com.example.tolan.models.ModelLevel;

import java.util.ArrayList;
import java.util.List;

public class AdpAutocompleteLevel extends ArrayAdapter<ModelLevel> {

    private List<ModelLevel> listaFull;

    public AdpAutocompleteLevel(@NonNull Context context, @NonNull List<ModelLevel> lista) {
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

        ModelLevel level = getItem(position);
        if(level != null){
            txtnom.setText(level.getNombre());
        }
        return convertView;
    }

    private Filter listaFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults results = new FilterResults();
            List<ModelLevel> suggestions = new ArrayList<>();
            if(charSequence == null || charSequence.length() == 0){
                suggestions.addAll(listaFull);
            }
            else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for(ModelLevel level : listaFull){
                    if(level.getNombre().toLowerCase().contains(filterPattern))
                        suggestions.add(level);
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
            return (((ModelLevel) resultValue).getNombre());
        }
    };
}
