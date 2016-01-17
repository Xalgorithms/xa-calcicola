package org.lichen.garni.activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.lichen.garni.GarniApp;
import org.lichen.garni.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity
        extends RxActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.toolbar_main) Toolbar _toolbar;
    @Bind(R.id.drawer_layout) DrawerLayout _drawer;
    @Bind(R.id.view_main_nav) NavigationView _nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GarniApp.object_graph(this).inject(this);
        ButterKnife.bind(this);

        setSupportActionBar(_toolbar);
/*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, _drawer, _toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        _drawer.setDrawerListener(toggle);
        toggle.syncState();

        _nav.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        if (!maybeCloseDrawer()) {
            super.onBackPressed();
        }
    }

    private boolean maybeCloseDrawer() {
        boolean rv = _drawer.isDrawerOpen(GravityCompat.START);
        if (rv) {
            _drawer.closeDrawer(GravityCompat.START);
        }
        return rv;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.navitem_main_transactions:
                TransactionsFragment frag = new TransactionsFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.layout_main_frame, frag);
                ft.commit();
                break;
        }

        _drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
