package com.androidbelieve.drawerwithswipetabs;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar1 = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        if (toolbar1 != null) {
            setSupportActionBar(toolbar1);
        }
        /**
         *Setup the DrawerLayout and NavigationView
         */
             mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
             mNavigationView = (NavigationView) findViewById(R.id.shitstuff) ;

        /**
         * Lets inflate the very first fragment
         * Here , we are inflating the TabFragment as the first Fragment
         */
             mFragmentManager = getSupportFragmentManager();
             mFragmentTransaction = mFragmentManager.beginTransaction();
             mFragmentTransaction.replace(R.id.containerView,new TabFragment()).commit();

        /**
         * Setup click events on the Navigation View Items.
         */
             mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
             @Override
             public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();
                 if (menuItem.getItemId() ==R.id.priPol){
                     //PRIVACY POLICY
                     Snackbar.make(mDrawerLayout, "PRIVACY POLICY", Snackbar.LENGTH_SHORT).show();
                 }
                 if (menuItem.getItemId() ==R.id.tandC){
                     //TERMS n CONDITIONS
                     Snackbar.make(mDrawerLayout, "TERMS and CONDITIONS", Snackbar.LENGTH_SHORT).show();
                 }
                 if (menuItem.getItemId() ==R.id.abtUs){
                     //ABOUT US
                     Snackbar.make(mDrawerLayout, "ABOUT US", Snackbar.LENGTH_SHORT).show();
                 }
                 if (menuItem.getItemId() ==R.id.logOut){
                     //LOG OUT
                     Snackbar.make(mDrawerLayout, "LOG OUT", Snackbar.LENGTH_SHORT).show();

                 }
                 return false;
            }
        });

                android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
                ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout, toolbar,R.string.app_name, R.string.app_name);

                mDrawerLayout.setDrawerListener(mDrawerToggle);

                mDrawerToggle.syncState();

    }

}