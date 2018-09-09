package com.alexandr4.loftmoney;


import android.content.Intent;
import android.os.Bundle;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

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
    //private FloatingActionButton fab;

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
/*        fab = view.findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), AddActivity.class);
                intent.putExtra(AddActivity.KEY_TYPE, type);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });*/

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

// option No. 2
/*    private void loadItems() {
        @SuppressLint("StaticFieldLeak")
        AsyncTask<Void, Void, List<Item>> asyncTask = new AsyncTask<Void, Void, List<Item>>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected List<Item> doInBackground(Void... voids) {
                Call call = api.getItems(type);

                try {
                    Response<List<Item>> response = call.execute();
                    List<Item> items = response.body();
                    return items;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<Item> items) {
                if (items != null) {
                    adapter.setItems(items);
                }
            }
        };

        asyncTask.execute();
    }
*/

// option No. 1
/*    private void loadItems() {
        new LoadItemsTask().start();
    }

    private class LoadItemsTask implements Runnable, Handler.Callback {
        private Thread thread;
        private Handler handler;

        public LoadItemsTask() {
            thread = new Thread(this);
            handler = new Handler(this);
        }

        public void start() {
            thread.start();
        }

        @Override
        public void run() {
            Call call = api.getItems(type);

            try {
                Response<List<Item>> response = call.execute();
                List<Item> items = response.body();
                Message message = handler.obtainMessage(111, items);
                message.sendToTarget();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

            @Override
            public boolean handleMessage (Message msg){
                if (msg.what == 111) {
                    List<Item> items = (List<Item>) msg.obj;
                    adapter.setItems(items);
                    return true;
                }
                return false;
            }
        }
*/

}
