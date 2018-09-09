package com.alexandr4.loftmoney;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    public static final int REQUEST_CODE = 100;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private MainPagesAdapter adapter;
    private Toolbar toolbar;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(TAG, "onCreate: ");

        toolbar = findViewById(R.id.toolbar);
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);
        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int page = viewPager.getCurrentItem();
                String type = null;
                switch (page) {
                    case MainPagesAdapter.PAGE_INCOME:
                        type = Item.TYPE_INCOME;
                        break;
                    case MainPagesAdapter.PAGE_EXPENSE:
                        type = Item.TYPE_EXPENSE;
                        break;
                }
                if (type != null) {

                    Intent intent = new Intent(MainActivity.this, AddActivity.class);
                    intent.putExtra(AddActivity.KEY_TYPE, type);
                    startActivityForResult(intent, REQUEST_CODE);
                }
            }
        });


        setSupportActionBar(toolbar);
        adapter = new MainPagesAdapter(getSupportFragmentManager(), this);

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new PageListener());
    }

    private void setActionBar(Toolbar toolbar) {
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
    }

    class PageListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case MainPagesAdapter.PAGE_INCOME:
                case MainPagesAdapter.PAGE_EXPENSE:
                    fab.show();
                    break;
                case MainPagesAdapter.PAGE_BALANCE:
                    fab.hide();
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    // translate to fragment
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
