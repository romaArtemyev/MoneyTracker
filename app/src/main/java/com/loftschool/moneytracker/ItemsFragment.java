package com.loftschool.moneytracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.loftschool.moneytracker.api.AddResult;
import com.loftschool.moneytracker.api.Api;
import com.loftschool.moneytracker.api.Result;

import java.io.IOException;
import java.util.List;

public class ItemsFragment extends Fragment implements ConfirmationDialog.ConfirmationDialogListener{

    private static final String KEY_TYPE = "TYPE";

    private String type;
    private ItemsAdapter adapter;
    private Api api;
    private ActionMode actionMode;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        type = getArguments().getString(KEY_TYPE, Item.TYPE_UNKNOWN);

        if (type.equals(Item.TYPE_UNKNOWN)) {
            throw new IllegalStateException("Unknown type");
        }

        adapter = new ItemsAdapter();
        api = ((App)getActivity().getApplication()).getApi();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_items, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        final FloatingActionButton addBtn = view.findViewById(R.id.f_addBtn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddActivity.startForResult(ItemsFragment.this, type, AddActivity.RC_ADD_ITEM);
            }
        });

        RecyclerView recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        adapter.setListener(new ItemsAdapterListener() {
            @Override
            public void onItemClick(Item item, int pos) {
                if (actionMode != null) {
                    adapter.toggleSelection(pos);
                    actionMode.setTitle(String.format(getString(R.string.action_mode_title), adapter.getSelectedItems().size()));
                }
            }

            @Override
            public void onItemLongClick(Item item, int pos) {
                if (actionMode != null) return;

                actionMode = ((AppCompatActivity)getActivity()).startSupportActionMode(new ActionMode.Callback() {
                    @Override
                    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                        mode.getMenuInflater().inflate(R.menu.items_menu, menu);
                        addBtn.hide();
                        return true;
                    }

                    @Override
                    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                        return false;
                    }

                    @Override
                    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_delete:
                                showDialog();
                                return true;
                            default:
                                return false;
                        }
                    }

                    @Override
                    public void onDestroyActionMode(ActionMode mode) {
                        actionMode = null;
                        adapter.clearSelectedItems();
                        addBtn.show();
                    }
                });
            adapter.toggleSelection(pos);
            actionMode.setTitle(String.format(getString(R.string.action_mode_title), adapter.getSelectedItems().size()));
            }
        });

        loadItems ();
    }

    public static ItemsFragment getItemsFragment(String type) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TYPE, type);
        ItemsFragment fragment = new ItemsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static final int LOADER_ITEMS = 0;
    public static final int ADD_ITEM = 1;
    public static final int REMOVE_ITEM = 2;

    public void loadItems() {
        getLoaderManager().initLoader(LOADER_ITEMS, null, new LoaderManager.LoaderCallbacks<List<Item>>() {
            @Override
            public Loader<List<Item>> onCreateLoader(int id, Bundle args) {
                return new AsyncTaskLoader<List<Item>>(getContext()) {
                    @Override
                    public List<Item> loadInBackground() {
                        try {
                            return api.items(type).execute().body();
                        } catch (IOException e) {
                            showError(e.getMessage());
                            return null;
                        }
                    }
                };
            }

            @Override
            public void onLoadFinished(Loader<List<Item>> loader, List<Item> items) {
                if (items == null) {
                    showError("error");
                } else {
                    adapter.setItems(items);
                }
            }

            @Override
            public void onLoaderReset(Loader<List<Item>> loader) {

            }
        }).forceLoad();
    }

    private void addItem (final Item item) {
        getLoaderManager().restartLoader(ADD_ITEM, null, new LoaderManager.LoaderCallbacks<AddResult>() {
            @Override
            public Loader<AddResult> onCreateLoader(int id, Bundle args) {
                return new AsyncTaskLoader<AddResult>(getContext()) {
                    @Override
                    public AddResult loadInBackground() {
                        try {
                            return api.add(item.price, item.name, item.type).execute().body();
                        } catch (IOException e) {
                            e.printStackTrace();
                            return null;
                        }
                    }
                };
            }

            @Override
            public void onLoadFinished(Loader<AddResult> loader, AddResult itemResult) {
                adapter.updeteID(item, itemResult.id);
            }

            @Override
            public void onLoaderReset(Loader<AddResult> loader) {

            }
        }).forceLoad();
    }

    private void removeItem (final Item item) {
        getLoaderManager().restartLoader(REMOVE_ITEM, null, new LoaderManager.LoaderCallbacks<Result>() {
            @Override
            public Loader<Result> onCreateLoader(int id, Bundle args) {
                return new AsyncTaskLoader<Result>(getContext()) {
                    @Override
                    public Result loadInBackground() {
                        try {
                            return api.remove(item.id).execute().body();
                        } catch (IOException e) {
                            showError(e.getMessage());
                            return null;
                        }
                    }
                };
            }

            @Override
            public void onLoadFinished(Loader<Result> loader, Result data) {

            }

            @Override
            public void onLoaderReset(Loader<Result> loader) {

            }
        }).forceLoad();
    }

    public void showError (String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AddActivity.RC_ADD_ITEM && resultCode == AddActivity.RESULT_OK) {
            Item item = (Item) data.getSerializableExtra(AddActivity.RESULT_ITEM);
            addItem(item);
        }
    }

    private void removeSelectedItems() {
        for (int i = adapter.getSelectedItems().size()-1; i >= 0; i--) {
            removeItem(adapter.remove(adapter.getSelectedItems().get(i)));
        }
    }

    private void showDialog() {
        DialogFragment dialogFragment = new ConfirmationDialog();
        dialogFragment.setTargetFragment(this, 300);
        dialogFragment.show(getFragmentManager(), "DialogFragment");
    }

    @Override
    public void onDialogYes(DialogFragment dialogFragment) {
        removeSelectedItems();
        actionMode.finish();
    }

    @Override
    public void onDialogNo(DialogFragment dialogFragment) {
        actionMode.finish();
    }
}
