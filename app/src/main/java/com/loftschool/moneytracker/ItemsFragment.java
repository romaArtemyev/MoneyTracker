package com.loftschool.moneytracker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ItemsFragment extends Fragment {

    public static final int TYPE_SPENDING = 0;
    public static final int TYPE_INCOMES = 1;
    private static final String KEY_TYPE = "TYPE";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_items, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new ItemsAdapter());
    }

    public static ItemsFragment getItemsFragment(int type) {
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_TYPE, type);
        ItemsFragment fragment = new ItemsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}
