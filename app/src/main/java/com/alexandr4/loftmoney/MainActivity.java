package com.alexandr4.loftmoney;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 100;

    public TabLayout tabLayout;
    private ViewPager viewPager;
    private MainPagesAdapter adapter;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private ActionMode actionMode;

    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);
        fab = findViewById(R.id.fab);

        App app = (App) getApplication();
        if (!app.isLoggedIn()) {
            logout();
        }

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

    private void logout() {
        startActivity(new Intent(this, AuthActivity.class));
    }

    @Override
    public void onSupportActionModeFinished(@NonNull ActionMode mode) {
        super.onSupportActionModeFinished(mode);
        actionMode = mode;
        tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        fab.show();
    }

    @Override
    public void onSupportActionModeStarted(@NonNull ActionMode mode) {
        super.onSupportActionModeStarted(mode);
        actionMode = mode;
        tabLayout.setBackgroundColor(getResources().getColor(R.color.action_mode_color));
        fab.hide();
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
            if (actionMode != null) {
                actionMode.finish();
            }
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
