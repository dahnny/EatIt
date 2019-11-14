package com.danielogbuti.eatit.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.danielogbuti.eatit.Interface.ItemClickListener;
import com.danielogbuti.eatit.R;

//This class serves as a view holder for the recycler view
public class menuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

   public TextView menu_name;
   public ImageView menu_image;

    private ItemClickListener itemClickListener;

    public menuViewHolder(@NonNull View itemView) {
        super(itemView);

        menu_name = (TextView)itemView.findViewById(R.id.menu_name);
        menu_image = (ImageView)itemView.findViewById(R.id.menu_image);

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
