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

import java.util.Collections;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemsViewHolder>{
    private List<Item> items = Collections.emptyList();

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
            name.setText(item.name);
            String string = String.format(context.getResources().getString(R.string.price), item.price);
            Spannable spannable = new SpannableString(string);
            spannable.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.white)), string.length()-1, string.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            price.setText(spannable);
        }
    }

    public void setItems (List<Item> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void addItem (Item item, int id) {
        this.items.add(id, item);
        notifyDataSetChanged();
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
