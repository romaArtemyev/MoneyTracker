package com.loftschool.moneytracker;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.loftschool.moneytracker.api.BalanceResult;

import java.io.IOException;

public class BalanceFragment extends Fragment {

    private TextView balance;
    private TextView expense;
    private TextView income;
    private DiagramView diagram;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_balance, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        balance = view.findViewById(R.id.balance);
        expense = view.findViewById(R.id.spending);
        income = view.findViewById(R.id.incomes);
        diagram = view.findViewById(R.id.diagram);
    }

    public void updateData() {
        getLoaderManager().restartLoader(0, null, new LoaderManager.LoaderCallbacks<BalanceResult>() {
            @Override
            public Loader<BalanceResult> onCreateLoader(int id, Bundle args) {
                return new AsyncTaskLoader<BalanceResult>(getContext()) {
                    @Override
                    public BalanceResult loadInBackground() {
                        try {
                            return ((App)getActivity().getApplication()).getApi().balance().execute().body();
                        } catch (IOException e) {
                            e.printStackTrace();
                            return null;
                        }
                    }
                };
            }

            @Override
            public void onLoadFinished(Loader<BalanceResult> loader, BalanceResult data) {
                if (data != null) {
                    balance.setText(getString(R.string.price, data.totalIncome - data.totalExpenses));
                    income.setText(getString(R.string.price, data.totalIncome));
                    expense.setText(getString(R.string.price, data.totalExpenses));
                    diagram.update(data.totalExpenses, data.totalIncome);
                } else {
                    Toast.makeText(getContext(), "error", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onLoaderReset(Loader<BalanceResult> loader) {

            }
        }).forceLoad();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed()) {
            updateData();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateData();
    }
}
