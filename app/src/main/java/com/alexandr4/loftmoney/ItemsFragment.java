package com.alexandr4.loftmoney;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.zip.Inflater;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class ItemsFragment extends Fragment {

    private static final String KEY_TYPE = "type";
    public static final int REQUEST_CODE = 100;

    private static final int LOADER_ITEMS = 0;
    private static final int LOADER_ADD = 1;
    private static final int LOADER_REMOVE = 2;

    private static final String TAG = "App";

    public static ItemsFragment newInstance(String type) {
        ItemsFragment fragment = new ItemsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ItemsFragment.KEY_TYPE, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    public ItemsFragment() {
        // Required empty public constructor
    }

    private RecyclerView recycler;
    private ItemsAdapter adapter;
    private String type;
    private static Api api;
    private SwipeRefreshLayout refresh;
    private ActionMode actionMode;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        type = args.getString(KEY_TYPE);

        api = ((App) getActivity().getApplication()).getApi();
        adapter = new ItemsAdapter();
        adapter.setListener(new AdapterListener());
        //   loadItems();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_items, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        refresh = view.findViewById(R.id.refresh);
        refresh.setColorSchemeColors(
                ContextCompat.getColor(requireContext(), R.color.apple_green),
                ContextCompat.getColor(requireContext(), R.color.colorAccent),
                ContextCompat.getColor(requireContext(), R.color.dark_sky_blue)
        );

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                loadItems();
            }
        });
        recycler = view.findViewById(R.id.recycler);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        loadItems();
    }

    private void loadItems() {
        Call<List<Item>> call = api.getItems(type);
        call.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(@NonNull Call<List<Item>> call, @NonNull Response<List<Item>> response) {
                refresh.setRefreshing(false);
                List<Item> items = response.body();
                adapter.setItems(items);
            }

            @Override
            public void onFailure(@NonNull Call<List<Item>> call, @NonNull Throwable t) {
                refresh.setRefreshing(false);
            }
        });
    }

    private void addItem(final Item item) {
        Call<PostResults> call = api.addItem(String.valueOf(item.price), item.name, item.type);
        call.enqueue(new Callback<PostResults>() {
            @Override
            public void onResponse(Call<PostResults> call, Response<PostResults> response) {
                PostResults postResult = response.body();
                if (postResult != null && postResult.isSuccess()) {
                    item.id = postResult.id;
                    adapter.addItem(item);
                }
            }

            @Override
            public void onFailure(Call<PostResults> call, Throwable t) {
            }
        });
    }

    //private void delItem(final int id) {
        public static void delItem(final int id) {
        Call<PostResults> call = api.removeItem(id);
        call.enqueue(new Callback<PostResults>() {
            @Override
            public void onResponse(Call<PostResults> call, Response<PostResults> response) {
                PostResults postResult = response.body();
              /*  if (postResult != null && postResult.isSuccess()) {
                    item.id = postResult.id;
                    adapter.removeItem(item);//-
                }*/
            }

            @Override
            public void onFailure(Call<PostResults> call, Throwable t) {

            }
        });
    }


    public void removeSelectedItems() {
        List<Integer> selected = adapter.getSelectedItems();
        int j = selected.size();
        for (int i = 0; i < j; i++) {
            if (adapter.removeItem(selected.get(i))) {
                i--;
                j--;
            }
        }
        actionMode.finish();
    }




/*
    private void removeItem(final int item_id) {
        getLoaderManager().initLoader(LOADER_REMOVE, null, new LoaderManager.LoaderCallbacks<PostResults>() {

            @Override
            public Loader<PostResults> onCreateLoader(int id, Bundle args) {

                return new AsyncTaskLoader<PostResults>(getContext()) {
                    @Override
                    public PostResults loadInBackground() {
                        try {
                            return api.removeItem(item_id).execute().body();
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    }
                };
            }

            @Override
            public void onLoadFinished(Loader<PostResults> loader, PostResults data) {
                if (data == null) {
                    Toast.makeText(getContext(), R.string.problem_authorization_message, Toast.LENGTH_SHORT).show();
                } else {
                    adapter.remove(data.id);
                    Toast.makeText(getContext(), "R.string.Remove", Toast.LENGTH_SHORT).show();
                }
                getLoaderManager().destroyLoader(LOADER_REMOVE);
            }

            @Override
            public void onLoaderReset(Loader<PostResults> loader) {
            }
        }).forceLoad();

    }

*/


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Item item = data.getParcelableExtra(AddActivity.KEY_ITEM);
            if (item.getType().equals(type)) {
                addItem(item);
            }
        }
    }


class AdapterListener implements ItemsAdapterListener {

    @Override
    public void onItemClick(Item item, int position) {
        if (actionMode == null) {
            return;
        }
        toggleItem(position);
    }

    @Override
    public void onItemLongClick(Item item, int position) {
        if (actionMode != null) {
            return;
        }
        ((AppCompatActivity) getActivity()).startSupportActionMode(new ActionModeCallback());
        toggleItem(position);
    }

    private void toggleItem(int position) {
        adapter.toggleItem(position);
    }
}

private class ActionModeCallback implements ActionMode.Callback {
    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        actionMode = mode;
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
       /* MenuInflater inflater = new MenuInflater(requireContext());
        inflater.inflate(R.menu.menu_action_mode, menu);*/
        MenuInflater inflater = new MenuInflater(requireContext());
        inflater.inflate(R.menu.menu_action_mode, menu);
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        if (item.getItemId() == R.id.menu_item_delete) {
            showConfirmationDialog();
            return true;
        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        adapter.clearSelections();
        actionMode = null;
    }

    private void showConfirmationDialog() {
        ConfirmDeleteDialog dialog = new ConfirmDeleteDialog();
        dialog.show(getFragmentManager(), null);
        dialog.setListener(new ConfirmDeleteDialog.Listener() {
            @Override
            public void onDeleteConfirmed() {
                removeSelectedItems();
            }
        });
    }
}


}
