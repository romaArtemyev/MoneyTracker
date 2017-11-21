package com.loftschool.moneytracker;

public interface ItemsAdapterListener {

    void onItemClick(Item item, int pos);

    void onItemLongClick(Item item, int pos);
}
