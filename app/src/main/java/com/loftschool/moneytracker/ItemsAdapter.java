package com.loftschool.moneytracker;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemsViewHolder>{
    private List<Item> items = Collections.emptyList();
    private ItemsAdapterListener listener;
    private SparseBooleanArray selectedItems = new SparseBooleanArray();

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

        private void bind(final Item item, final int pos, boolean selected, final ItemsAdapterListener listener) {
            name.setText(item.name);
            String string = String.format(context.getResources().getString(R.string.price), item.price);
            Spannable spannable = new SpannableString(string);
            spannable.setSpan(new AbsoluteSizeSpan(context.getResources().getDimensionPixelSize(R.dimen.rouble_size)), string.length()-1, string.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            price.setText(spannable);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item, pos);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onItemLongClick(item, pos);
                    return true;
                }
            });

            itemView.setActivated(selected);
        }
    }

    public void setItems (List<Item> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void setListener(ItemsAdapterListener listener) {
        this.listener = listener;
    }

    public void updeteID (Item item, int id) {
        item.id = id;
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
        holder.bind(item, position, selectedItems.get(position, false), listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    void toggleSelection (int pos) {
        if (selectedItems.get(pos, false)) {
            selectedItems.delete(pos);
        } else {
            selectedItems.put(pos, true);
        }
        notifyItemChanged(pos);
    }

    void clearSelectedItems() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<>(selectedItems.size());
        for (int i = 0; i <= selectedItems.size()-1; i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }

    Item remove(int pos) {
        Item item = items.remove(pos);
        return item;
    }
}
