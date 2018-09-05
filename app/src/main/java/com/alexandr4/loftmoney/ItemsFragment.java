package com.alexandr4.loftmoney;


import android.os.Bundle;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ItemsFragment extends Fragment {
    private static final String KEY_TYPE = "type";
    private static final int TYPE_UNCNOWN = -1;
    public static final int TYPE_INCOMES = 1;
    public static final int TYPE_EXPENSES = 2;
    public static final int TYPE_BALANCE = 3;

    public static ItemsFragment newInstance(int type) {
        ItemsFragment fragment = new ItemsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ItemsFragment.KEY_TYPE, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    public ItemsFragment() {
        // Required empty public constructor
    }

    private RecyclerView recycler;
    private ItemsAdapter adapter;
    private int type;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            type = args.getInt(KEY_TYPE, TYPE_UNCNOWN);
            if (type == TYPE_UNCNOWN) {
                throw new IllegalThreadStateException("Unknown type");
            }
        } else {
            throw new IllegalStateException("You mast pass valid fragment type");

        }

        adapter = new ItemsAdapter();
        if (type != TYPE_BALANCE) {
            adapter.setItems(createTestItems());
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_items, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recycler = view.findViewById(R.id.recycler);
        recycler.setAdapter(adapter);
        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    private List<ItemActivity> createTestItems() {
        List<ItemActivity> items = new ArrayList<>();
        items.add(new ItemActivity("Молоко", "70 р"));
        items.add(new ItemActivity("Зубная щетка", "70 р"));
        items.add(new ItemActivity("Сковорода с антипригарным покрытием", "10000 р"));
        items.add(new ItemActivity("Зубочистка", "2 р"));
        items.add(new ItemActivity("Лошадь", "100000 р"));
        items.add(new ItemActivity("Молоко", "70 р"));
        items.add(new ItemActivity("Зубная щетка", "70 р"));
        items.add(new ItemActivity("Сковорода с антипригарным покрытием", "10000 р"));
        items.add(new ItemActivity("Зубочистка", "2 р"));
        items.add(new ItemActivity("Лошадь", "100000 р"));
        items.add(new ItemActivity("Молоко", "70 р"));
        items.add(new ItemActivity("Зубная щетка", "70 р"));
        items.add(new ItemActivity("Сковорода с антипригарным покрытием", "10000 р"));
        items.add(new ItemActivity("Зубочистка", "2 р"));
        items.add(new ItemActivity("Лошадь", "100000 р"));
        items.add(new ItemActivity("Молоко", "70 р"));
        items.add(new ItemActivity("Зубная щетка", "70 р"));
        items.add(new ItemActivity("Сковорода с антипригарным покрытием", "10000 р"));
        items.add(new ItemActivity("Зубочистка", "2 р"));
        items.add(new ItemActivity("Лошадь", "100000 р"));
        return items;

    }

}
