package com.danielogbuti.eatit.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.danielogbuti.eatit.Interface.ItemClickListener;
import com.danielogbuti.eatit.R;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView orderId, orderStatus,orderPhone,orderAddress;

    private ItemClickListener itemClickListener;

    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);

        orderId = (TextView)itemView.findViewById(R.id.order_id);
        orderStatus = (TextView)itemView.findViewById(R.id.order_status);
        orderPhone = (TextView)itemView.findViewById(R.id.order_phone);
        orderAddress = (TextView)itemView.findViewById(R.id.order_address);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);

    }
}
