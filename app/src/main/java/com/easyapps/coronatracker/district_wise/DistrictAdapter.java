package com.easyapps.coronatracker.district_wise;

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

public class DistrictAdapter extends RecyclerView.Adapter<DistrictAdapter.DistrictViewHolder> implements Filterable {

    private ArrayList<DistrictItem> mDistrictList;
    private ArrayList<DistrictItem> mDistrictListFull;
    private OnItemClickListener mListener;

    @Override
    public Filter getFilter() {
        return districtFilter;
    }

    private Filter districtFilter=new Filter(){
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<DistrictItem> filteredList=new ArrayList<>();

            if(constraint==null||constraint.length()==0){
                filteredList.addAll(mDistrictListFull);
            }else{
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(DistrictItem item: mDistrictList){
                    if(item.getText1().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values=filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mDistrictList.clear();
            mDistrictList.addAll((ArrayList)results.values);
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
    public DistrictViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.district_item, parent, false);
        return new DistrictViewHolder(v, mListener);
    }

    public DistrictAdapter(DistrictActivity stateWiseActivity, ArrayList<DistrictItem> stateList) {
        mDistrictList = stateList;
        mDistrictListFull=new ArrayList<>(mDistrictList);
    }

    @Override
    public void onBindViewHolder(@NonNull DistrictViewHolder holder, int position) {
        DistrictItem currentItem = mDistrictList.get(position);

        holder.mTextView1.setText(currentItem.getText1());
        holder.mTextView2.setText(currentItem.getText2());
        holder.mTextView3.setText(currentItem.getText3());
    }

    @Override
    public int getItemCount() {
        return mDistrictList.size();
    }

    public static class DistrictViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;

        public DistrictViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            mTextView1 = itemView.findViewById(R.id.state_name);
            mTextView2 = itemView.findViewById(R.id.district_name);
            mTextView3 = itemView.findViewById(R.id.confirmed_district);

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
