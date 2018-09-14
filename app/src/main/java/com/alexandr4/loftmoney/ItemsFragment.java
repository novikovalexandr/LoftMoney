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
import android.support.v4.content.ContextCompat;
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

    private static final String TAG = "ItemsFragment";
    private static final String KEY_TYPE = "type";
    public static final int REQUEST_CODE = 100;

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
    private Api api;
    private SwipeRefreshLayout refresh;
    private ActionMode actionMode;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: ");
        Bundle args = getArguments();
        type = args.getString(KEY_TYPE);

        api = ((App) getActivity().getApplication()).getApi();
       /* Application application = getActivity().getApplication();
        App app = (App) application;
        api = app.getApi();*/

        adapter = new ItemsAdapter();
        adapter.setListener(new AdapterListener());
        loadItems();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i(TAG, "onCreateView: ");
        return inflater.inflate(R.layout.fragment_items, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onViewCreated: ");
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
    }

    @Override
    public void onDestroyView() {
        Log.i(TAG, "onDestroyView: ");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: ");
        super.onDestroy();
    }

    // option No. 3
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Item item = data.getParcelableExtra(AddActivity.KEY_ITEM);
            if (item.getType().equals(type)) {
                adapter.addItem(item);
            }
        }
    }

    private void removeSelectedItems() {
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

    class AdapterListener implements ItemsAdapterListener {

        @Override
        public void onItemClick(Item item, int position) {
            Log.i(TAG, "onItemClick: name = " + item.getName() + " position: " + position);
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
            Log.i(TAG, "onCreateActionMode: ");
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
