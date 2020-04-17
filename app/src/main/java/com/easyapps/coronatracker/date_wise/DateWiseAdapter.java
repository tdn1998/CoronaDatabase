package com.easyapps.coronatracker.date_wise;

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

public class DateWiseAdapter extends RecyclerView.Adapter<DateWiseAdapter.DateViewHolder> implements Filterable {

    private ArrayList<DateWiseItem> mDateList;
    private ArrayList<DateWiseItem> mDateListFull;
    private OnItemClickListener mListener;

    @Override
    public Filter getFilter() {
        return dateFilter;
    }

    private Filter dateFilter=new Filter(){
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<DateWiseItem> filteredList=new ArrayList<>();

            if(constraint==null||constraint.length()==0){
                filteredList.addAll(mDateListFull);
            }else{
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(DateWiseItem item: mDateList){
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
            mDateList.clear();
            mDateList.addAll((ArrayList)results.values);
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
    public DateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.date_wise_item, parent, false);
        return new DateViewHolder(v, mListener);
    }

    public DateWiseAdapter(DateWiseActivity dateWiseActivity, ArrayList<DateWiseItem> dateList) {
        mDateList = dateList;
        mDateListFull=new ArrayList<>(mDateList);
    }

    @Override
    public void onBindViewHolder(@NonNull DateViewHolder holder, int position) {
        DateWiseItem currentItem = mDateList.get(position);

        holder.mTextView1.setText("On Date: "+currentItem.getText1());
        holder.mTextView2.setText(currentItem.getText2());
        holder.mTextView3.setText(currentItem.getText3());
        holder.mTextView4.setText(currentItem.getText4());
        holder.mTextView5.setText(currentItem.getText5());
        holder.mTextView6.setText(currentItem.getText6());
        holder.mTextView7.setText(currentItem.getText7());
    }

    @Override
    public int getItemCount() {
        return mDateList.size();
    }

    public static class DateViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;
        public TextView mTextView4;
        public TextView mTextView5;
        public TextView mTextView6;
        public TextView mTextView7;

        public DateViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            mTextView1 = itemView.findViewById(R.id.date);
            mTextView2 = itemView.findViewById(R.id.data_confirmed);
            mTextView3 = itemView.findViewById(R.id.data_recovered);
            mTextView4 = itemView.findViewById(R.id.data_deceased);
            mTextView5 = itemView.findViewById(R.id.data_confirmed_on);
            mTextView6 = itemView.findViewById(R.id.data_recovered_on);
            mTextView7 = itemView.findViewById(R.id.data_deceased_on);

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
