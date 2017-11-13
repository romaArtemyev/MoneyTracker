package com.loftschool.moneytracker;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemsViewHolder>{
    private ArrayList<Item> items = new ArrayList<>();

    static class ItemsViewHolder extends RecyclerView.ViewHolder {
        private Context context;
        private TextView name;
        private TextView price;

        private ItemsViewHolder(View itemView, Context context) {
            super(itemView);
            name = itemView.findViewById(R.id.item_name);
            price = itemView.findViewById(R.id.item_price);
            this.context = context;
        }

        private void bind(Item item) {
            name.setText(item.getName());
            String string = String.format(context.getResources().getString(R.string.price), item.getPrice());
            Spannable spannable = new SpannableString(string);
            spannable.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.white)), string.length()-1, string.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            price.setText(spannable);
        }
    }

    ItemsAdapter() {
        items.add(new Item("Молоко", 35));
        items.add(new Item("Сковорода с антипригарным покрытием", 1500));
        items.add(new Item("Зубная щетка", 55));
        items.add(new Item("Молоко", 35));
        items.add(new Item("Молоко", 35));
        items.add(new Item("Сковорода с антипригарным покрытием", 1500));
        items.add(new Item("Зубная щетка", 55));
        items.add(new Item("Молоко", 35));
        items.add(new Item("Молоко", 35));
        items.add(new Item("Сковорода с антипригарным покрытием", 1500));
        items.add(new Item("Зубная щетка", 55));
        items.add(new Item("Молоко", 35));
        items.add(new Item("Сковорода с антипригарным покрытием", 1500));
        items.add(new Item("Зубная щетка", 55));
        items.add(new Item("Молоко", 35));
        items.add(new Item("Молоко", 35));
        items.add(new Item("Сковорода с антипригарным покрытием", 1500));
        items.add(new Item("Зубная щетка", 55));
        items.add(new Item("Молоко", 35));
        items.add(new Item("Молоко", 35));
        items.add(new Item("Сковорода с антипригарным покрытием", 1500));
        items.add(new Item("Зубная щетка", 55));
    }

    @Override
    public ItemsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ItemsViewHolder(view, parent.getContext());
    }

    @Override
    public void onBindViewHolder(ItemsViewHolder holder, int position) {
        Item item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
