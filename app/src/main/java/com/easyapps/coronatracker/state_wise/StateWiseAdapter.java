package com.easyapps.coronatracker.state_wise;

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

public class StateWiseAdapter extends RecyclerView.Adapter<StateWiseAdapter.StateViewHolder> implements Filterable {

    private ArrayList<StateWiseItem> mStateList;
    private ArrayList<StateWiseItem> mStateListFull;
    private OnItemClickListener mListener;

    @Override
    public Filter getFilter() {
        return stateFilter;
    }

    private Filter stateFilter=new Filter(){
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<StateWiseItem> filteredList=new ArrayList<>();

            if(constraint==null||constraint.length()==0){
                filteredList.addAll(mStateListFull);
            }else{
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(StateWiseItem item: mStateList){
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
            mStateList.clear();
            mStateList.addAll((ArrayList)results.values);
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
    public StateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.state_wise_item, parent, false);
        StateViewHolder svh = new StateViewHolder(v, mListener);
        return svh;
    }

    public StateWiseAdapter(StateWiseActivity stateWiseActivity, ArrayList<StateWiseItem> stateList) {
        mStateList = stateList;
        mStateListFull=new ArrayList<>(mStateList);
    }

    @Override
    public void onBindViewHolder(@NonNull StateViewHolder holder, int position) {
        StateWiseItem currentItem = mStateList.get(position);

        holder.mTextView1.setText(currentItem.getText1());
        holder.mTextView2.setText("Updated: "+currentItem.getText2());
        holder.mTextView3.setText(currentItem.getText3());
        holder.mTextView4.setText(currentItem.getText4());
        holder.mTextView5.setText(currentItem.getText5());
        holder.mTextView6.setText(currentItem.getText6());
        holder.mTextView7.setText(currentItem.getText7());
        holder.mTextView8.setText(currentItem.getText8());
        holder.mTextView9.setText(currentItem.getText9());
    }

    @Override
    public int getItemCount() {
        return mStateList.size();
    }

    public static class StateViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;
        public TextView mTextView4;
        public TextView mTextView5;
        public TextView mTextView6;
        public TextView mTextView7;
        public TextView mTextView8;
        public TextView mTextView9;

        public StateViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            mTextView1 = itemView.findViewById(R.id.state_name);
            mTextView2 = itemView.findViewById(R.id.state_updated);
            mTextView3 = itemView.findViewById(R.id.data_confirmed_state);
            mTextView4 = itemView.findViewById(R.id.data_active_state);
            mTextView5 = itemView.findViewById(R.id.data_recovered_state);
            mTextView6 = itemView.findViewById(R.id.data_deceased_state);
            mTextView7 = itemView.findViewById(R.id.data_confirmed_on_state);
            mTextView8 = itemView.findViewById(R.id.data_recovered_on_state);
            mTextView9 = itemView.findViewById(R.id.data_deceased_on_state);

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
