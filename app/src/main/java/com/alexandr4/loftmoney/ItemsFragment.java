package com.alexandr4.loftmoney;


import android.annotation.SuppressLint;
import android.app.Application;
import android.os.AsyncTask;
import android.os.Bundle;


import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class ItemsFragment extends Fragment {

    private static final String TAG = "ItemsFragment";
    private static final String KEY_TYPE = "type";

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
        Call<List<ItemActivity>> call = api.getItems(type);

        call.enqueue(new Callback<List<ItemActivity>>() {
            @Override
            public void onResponse(@NonNull Call<List<ItemActivity>> call, @NonNull Response<List<ItemActivity>> response) {
                List<ItemActivity> items = response.body();
                adapter.setItems(items);
            }

            @Override
            public void onFailure(@NonNull Call<List<ItemActivity>> call, @NonNull Throwable t) {

            }
        });

    }

// option No. 2
/*    private void loadItems() {
        @SuppressLint("StaticFieldLeak")
        AsyncTask<Void, Void, List<ItemActivity>> asyncTask = new AsyncTask<Void, Void, List<ItemActivity>>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected List<ItemActivity> doInBackground(Void... voids) {
                Call call = api.getItems(type);

                try {
                    Response<List<ItemActivity>> response = call.execute();
                    List<ItemActivity> items = response.body();
                    return items;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<ItemActivity> items) {
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
                Response<List<ItemActivity>> response = call.execute();
                List<ItemActivity> items = response.body();
                Message message = handler.obtainMessage(111, items);
                message.sendToTarget();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

            @Override
            public boolean handleMessage (Message msg){
                if (msg.what == 111) {
                    List<ItemActivity> items = (List<ItemActivity>) msg.obj;
                    adapter.setItems(items);
                    return true;
                }
                return false;
            }
        }
*/

}
