package com.danielogbuti.eatit.Interface;

import android.view.View;

public abstract class ItemClickListener {
    public abstract void onClick(View view, int position, boolean isLongClick);
}
