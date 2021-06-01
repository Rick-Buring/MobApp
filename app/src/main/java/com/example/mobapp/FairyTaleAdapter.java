package com.example.mobapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;


public class FairyTaleAdapter extends RecyclerView.Adapter<FairyTaleAdapter.FairytaleViewHolder> {

    private final OnItemClickListener clickListener;

    public interface OnItemClickListener {
        void onItemClick(int clickedPosition);
    }

    public FairyTaleAdapter(OnItemClickListener clickListener) {
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
            this.binding.executePendingBindings();
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            clickListener.onItemClick(pos);
        }
    }

    @Override
    public FairytaleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.fairytale_view_item, parent, false);

        return new FairytaleViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(FairyTaleAdapter.FairytaleViewHolder holder, int position) {
        Fairytale fairytale = Fairytale.fairytales[position];
        holder.bind(fairytale);
    }

    @Override
    public int getItemCount() {
        return Fairytale.fairytales.length;
    }


}
