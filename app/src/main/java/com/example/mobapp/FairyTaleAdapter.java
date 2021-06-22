package com.example.mobapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobapp.fairytale.Fairytale;


public class FairyTaleAdapter extends RecyclerView.Adapter<FairyTaleAdapter.FairytaleViewHolder> {

    private final OnItemClickListener clickListener;

    /**
     * Interface for the class that implements the onClick
     */
    public interface OnItemClickListener {
        void onItemClick(int clickedPosition);
    }

    /**
     * Constructor for FairytaleAdapter
     * @param clickListener  The listener to send the click action to
     */
    public FairyTaleAdapter(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    /**
     * Makes sure the items are correctly inserted in the recyclerView
     */
    public class FairytaleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final ViewDataBinding binding;

        public FairytaleViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.itemView.setOnClickListener(this);
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
