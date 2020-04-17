package com.easyapps.coronatracker.country_wise;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.easyapps.coronatracker.R;

import java.util.ArrayList;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.CountryViewHolder> implements Filterable {

    private ArrayList<CountryItem> mCountryList;
    private ArrayList<CountryItem> mCountryListFull;
    private OnItemClickListener mListener;

    @Override
    public Filter getFilter() {
        return countryFilter;
    }

    private Filter countryFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<CountryItem> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(mCountryListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (CountryItem item : mCountryList) {
                    if (item.getText1().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mCountryList.clear();
            mCountryList.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public CountryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.country_item, parent, false);
        return new CountryViewHolder(v, mListener);
    }

    public CountryAdapter(CountryActivity countryActivity, ArrayList<CountryItem> stateList) {
        mCountryList = stateList;
        mCountryListFull = new ArrayList<>(mCountryList);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryViewHolder holder, int position) {
        CountryItem currentItem = mCountryList.get(position);

        holder.mTextView1.setText(currentItem.getText1());
        holder.mTextView2.setText("Updated: "+currentItem.getText2());
        holder.mTextView3.setText(currentItem.getText3());
        holder.mTextView4.setText(currentItem.getText4());
        holder.mTextView5.setText(currentItem.getText5());
    }

    @Override
    public int getItemCount() {
        return mCountryList.size();
    }

    public static class CountryViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;
        public TextView mTextView4;
        public TextView mTextView5;

        public CountryViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            mTextView1 = itemView.findViewById(R.id.country_name);
            mTextView2 = itemView.findViewById(R.id.country_updated);
            mTextView3 = itemView.findViewById(R.id.country_confirmed);
            mTextView4 = itemView.findViewById(R.id.country_recovered);
            mTextView5 = itemView.findViewById(R.id.country_deceased);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
