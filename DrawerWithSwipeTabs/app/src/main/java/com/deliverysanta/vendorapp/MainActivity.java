package com.deliverysanta.vendorapp;

import android.content.Intent;
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

public class MainActivity extends AppCompatActivity
{
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        session = new SessionManager(MainActivity.this);
        Toolbar toolbar1 = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        if (toolbar1 != null) {
            setSupportActionBar(toolbar1);
            toolbar1.setTitle("Delivery Santa");
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

/*

                 if (menuItem.getItemId() ==R.id.reqUpd){
                     //REQUEST UPDATE
                     final AlertDialog.Builder dialog = new AlertDialog.Builder(mDrawerLayout.getContext());
                     LayoutInflater inflater=getLayoutInflater();
                     final View dialoglayout = inflater.inflate(R.layout.dish_update_box, null);
                     dialog.setView(dialoglayout);
                     dialog.setTitle("UPDATE DISH");
                     Button b1 = (Button)dialoglayout.findViewById(R.id.button2); //REMOVE
                     Button b2 = (Button)dialoglayout.findViewById(R.id.button3); //UPDATE
                     Button b3 = (Button)dialoglayout.findViewById(R.id.button4); //CANCEL
                     final AlertDialog alertDialog1 = dialog.create();
                     alertDialog1.show();

                     b1.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View v) {
                             alertDialog1.dismiss();
                             Snackbar.make(mDrawerLayout, "ITEM REMOVAL REQUESTED.", Snackbar.LENGTH_SHORT).show();
                         }
                     });
                     b2.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View v) {
                             alertDialog1.dismiss();
                             Snackbar.make(mDrawerLayout, "ITEM UPDATION REQUESTED.", Snackbar.LENGTH_SHORT).show();
                         }
                     });
                     b3.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View v) {
                             alertDialog1.dismiss();
                         }
                     });
                 }

                 if (menuItem.getItemId() ==R.id.notAbs){
                     //NOTIFY ABSENCE
                     Intent intent=new Intent(MainActivity.this,NotifyAbsenceActivity.class);
                     startActivity(intent);
                     Snackbar.make(mDrawerLayout, "ABSENCE NOTIFIED.", Snackbar.LENGTH_SHORT).show();

                 }
*/
                 if (menuItem.getItemId() ==R.id.priPol){
                     //REQUEST UPDATE
                     Snackbar.make(mDrawerLayout, "PRIVACY POLICY", Snackbar.LENGTH_SHORT).show();

                 }
                 if (menuItem.getItemId() ==R.id.tandC){
                     //REQUEST UPDATE
                     Snackbar.make(mDrawerLayout, "TERMS and CONDITIONS", Snackbar.LENGTH_SHORT).show();

                 }
                 if (menuItem.getItemId() ==R.id.abtUs){
                     //REQUEST UPDATE
                     Snackbar.make(mDrawerLayout, "ABOUT US", Snackbar.LENGTH_SHORT).show();

                 }
                 if (menuItem.getItemId() ==R.id.logOut){
                     //LOG OUT
                     session.setLogin(false);
                     Intent i = new Intent(MainActivity.this, LoginActivity.class);
                     startActivity(i);
                     finish();
                     Snackbar.make(mDrawerLayout, "LOG OUT", Snackbar.LENGTH_SHORT).show();
                 }

                 return false;
            }

        });

        /**
         * Setup Drawer Toggle of the Toolbar
         */

                android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
                ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout, toolbar,R.string.app_name, R.string.app_name);

                mDrawerLayout.setDrawerListener(mDrawerToggle);

                mDrawerToggle.syncState();

    }

}