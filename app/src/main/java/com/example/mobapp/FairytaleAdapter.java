package com.example.mobapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FairytaleAdapter extends RecyclerView.Adapter<FairytaleAdapter.FairytaleViewHolder> {

    private Context appContext;
    private List<Fairytale> fairytales;
    private OnItemClickListener clickListener;

    public interface OnItemClickListener {
        void onItemClick(int clickedPosition);
    }

    public FairytaleAdapter(Context appContext, List<Fairytale> fairytales, OnItemClickListener clickListener) {
        this.appContext = appContext;
        this.fairytales = fairytales;
        this.clickListener = clickListener;
    }

    public class FairytaleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final ViewDataBinding binding;

        public FairytaleViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            itemView.setOnClickListener(this);
        }

        public void bind(Object obj){
            this.binding.setVariable(BR.data, obj);
            binding.executePendingBindings();
        }

        @Override
        public void onClick(View v) {

        }
    }

    @Override
    public FairytaleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return null;
    }

    @Override
    public void onBindViewHolder(FairytaleAdapter.FairytaleViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }




}
