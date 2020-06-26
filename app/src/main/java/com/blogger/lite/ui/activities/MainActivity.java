package com.blogger.lite.ui.activities;

import android.content.Intent;
import android.graphics.Typeface;

import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

import com.blogger.lite.R;
import com.blogger.lite.ui.fragments.HomeFragment;
import com.blogger.lite.ui.fragments.SavedFragment;
import com.blogger.lite.ui.fragments.SettingsFragment;
import com.blogger.lite.ui.services.PostService;

public class MainActivity extends AppCompatActivity {

    NavigationView nav_view;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initLeftMenu();
        nav_view.getMenu().performIdentifierAction(R.id.left_item_home,0);

        //Start Background service
        Intent service = new Intent(this, PostService.class);
        this.startService(service);
    }

    private void initLeftMenu() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nav_view = findViewById(R.id.nav_view);

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        nav_view.setNavigationItemSelectedListener(item -> {

            Fragment frag;
            switch (item.getItemId()) {
                case R.id.left_item_home:
                    frag = HomeFragment.newInstance();
                    setFragment(frag);
                    setActionBarTitle(getString(R.string.app_name));
                    drawer.closeDrawers();
                    return true;
                case R.id.left_item_favorites:
                    frag = SavedFragment.newInstance();
                    setFragment(frag);
                    setActionBarTitle(getString(R.string.menu_favorites));
                    drawer.closeDrawers();
                    return true;
                case R.id.left_item_settings:
                    frag = SettingsFragment.newInstance();
                    setFragment(frag);
                    setActionBarTitle(getString(R.string.settings));
                    drawer.closeDrawers();
                    return true;
            }
            drawer.closeDrawers();
            return true;
        });

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_share, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void setFragment(Fragment fragment) {
        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.content_frame, fragment, fragment.getTag());
        t.commit();
    }

    private void setActionBarTitle(String title){
        SpannableStringBuilder ssb = new SpannableStringBuilder(title);
        getSupportActionBar().setTitle(ssb);
    }

}
