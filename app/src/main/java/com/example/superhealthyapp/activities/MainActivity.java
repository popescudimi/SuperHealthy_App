package com.example.superhealthyapp.activities;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.Canvas;
import android.content.Context;

import com.example.superhealthyapp.fragments.Food.FoodFragment;
import com.example.superhealthyapp.fragments.Home.HomeFragment;
import com.example.superhealthyapp.fragments.Pedometer.PedometerFragment;
import com.example.superhealthyapp.fragments.Alert.AlertFragment;
import com.example.superhealthyapp.fragments.Water.WaterFragment;
import com.example.superhealthyapp.R;
import com.google.android.material.tabs.TabLayout;
import com.ncapdevi.fragnav.FragNavController;

import static com.example.superhealthyapp.fragments.Home.LogoFragment.tabIconsSelected;

public class MainActivity extends BaseActivity implements
        FragNavController.RootFragmentListener {

    private Toolbar toolbar;
    private String[] TABS;
    private TabLayout bottomTabLayout;
    private FragNavController mNavController;
    private TextView toolbarTitleText;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar =  findViewById(R.id.toolbar);
        toolbarTitleText = toolbar.findViewById(R.id.toolbarTitle);

        bottomTabLayout =  findViewById(R.id.bottom_tab_layout);
        TABS = getResources().getStringArray(R.array.tab_name);
        mNavController = FragNavController.newBuilder(savedInstanceState, getSupportFragmentManager(), R.id.content_frame)
                .rootFragmentListener(this, TABS.length)
                .build();
        initializeToolbar();
        initializeTabs();
        switchTab(0);
        bottomTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switchTab(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                switchTab(tab.getPosition());
            }
        });

    }


    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    private void initializeToolbar() {
        setSupportActionBar(toolbar);
    }

    private Drawable toolBarPressed(int box, int pressed, Context context)
    {
        Drawable boxNotSelected, boxIsSelected;
        boxNotSelected = ContextCompat.getDrawable(context, box);
        boxIsSelected = ContextCompat.getDrawable(context, pressed);
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{android.R.attr.state_selected},
                boxIsSelected);
        drawable.addState(new int[]{android.R.attr.state_enabled},
                boxNotSelected);

        return drawable;
    }

    private void initializeTabs() {
        if (bottomTabLayout != null) {
            for (int i = 0; i < TABS.length; i++) {
                bottomTabLayout.addTab(bottomTabLayout.newTab());
                TabLayout.Tab tab = bottomTabLayout.getTabAt(i);
                if (tab != null) {
                    tab.setCustomView(getTabView(i));
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public View getTabView(int position) {
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.tabs, null);
        ImageView icon =  view.findViewById(R.id.tab_icon);
        icon.setImageDrawable(toolBarPressed(tabIconsSelected[position], tabIconsSelected[position], MainActivity.this));

        return view;
    }

    public void switchTab(int position) {
        mNavController.switchTab(position);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mNavController != null) {
            mNavController.onSaveInstanceState(outState);
        }
    }

    @Override
    public Fragment getRootFragment(int index) {
        switch (index) {
            case FragNavController.TAB1:
                return new HomeFragment();
            case FragNavController.TAB2:
                return new FoodFragment();
            case FragNavController.TAB3:
                return new WaterFragment();
            case FragNavController.TAB4:
                return new PedometerFragment();
            case FragNavController.TAB5:
                return new AlertFragment();

        }
        throw new IllegalStateException("Need to send an index that we know");
    }

    public void updateToolbarTitle(String title) {
        if(getSupportActionBar() != null)
            toolbarTitleText.setText(title);
    }

}