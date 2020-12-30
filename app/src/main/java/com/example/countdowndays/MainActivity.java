package com.example.countdowndays;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.media.MediaPlayer;
import android.os.Bundle;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.example.countdowndays.db.EventDB;
import com.example.countdowndays.db.EventOpenHelper;
import com.example.countdowndays.model.Event;

import java.util.ArrayList;
import java.util.List;


import github.chenupt.dragtoplayout.DragTopLayout;


public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private EditText edit_search_input;
    private List<Fragment> list = new ArrayList<Fragment>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar)findViewById(R.id.main_toolbar);
        if(toolbar != null){
            setSupportActionBar(toolbar);
        }

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.add_event:{
                        sendBroadcast(new Intent("com.example.countdowndays.add_event"));
                        DragTopLayout dtl = (DragTopLayout)findViewById(R.id.dtl);
                        viewPager.setCurrentItem(0);
                        dtl.toggleTopView();
                        ListView lv = (ListView)findViewById(R.id.listview);
                        lv.setSelection(0);
                        break;
                    }
                }
                return true;
            }
        });

        viewPager = (ViewPager)findViewById(R.id.viewpager);
        EventListFragment eventListFragment = new EventListFragment();
        list.add(eventListFragment);
        list.add(new DateCalmFrag());
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),list);
        viewPager.setAdapter(adapter);
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> list;

        public ViewPagerAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}