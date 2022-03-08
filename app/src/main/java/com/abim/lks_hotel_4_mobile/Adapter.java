package com.abim.lks_hotel_4_mobile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    Context ctx;
    List<Cart> carts;

    public Adapter(Context ctx, List<Cart> carts) {
        this.ctx = ctx;
        this.carts = carts;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_data, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder( Adapter.ViewHolder holder, int position) {
        Cart cart = carts.get(position);
        holder.tv_name.setText(String.valueOf(cart.getFdName()));
        holder.tv_price.setText(String.valueOf(cart.getTotal()));
    }

    @Override
    public int getItemCount() {
        return carts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tv_name, tv_price;
        Button btn_delete;

        public ViewHolder( View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.tv_name);
            tv_price = itemView.findViewById(R.id.tv_price);
            btn_delete = itemView.findViewById(R.id.btn_del);

            btn_delete.setOnClickListener(this::onClick);
        }

        @Override
        public void onClick(View view) {
            carts.remove(getAdapterPosition());
            notifyItemRemoved(getAdapterPosition());
            notifyItemRangeChanged(getAdapterPosition(), carts.size());
        }
    }
}
