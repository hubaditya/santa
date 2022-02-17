package delivery.santa.delivering.user;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;
import com.github.florent37.materialviewpager.sample.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import delivery.santa.delivery.customer.fragment.VendorFragment;
import delivery.santa.delivery.customer.fragment.VendorFragment1;
import delivery.santa.delivery.customer.fragment.VendorFragment2;
import delivery.santa.delivery.customer.fragment.VendorFragment3;
import delivery.santa.delivery.customer.fragment.VendorFragment4;
import delivery.santa.delivery.customer.fragment.VendorFragment5;
import delivery.santa.delivery.customer.fragment.VendorFragment6;
import delivery.santa.delivery.customer.fragment.VendorFragment7;
import delivery.santa.delivery.customer.fragment.VendorFragment8;
import delivery.santa.delivery.customer.fragment.VendorFragment9;

public class MainActivity extends AppCompatActivity
{
    private MaterialViewPager mViewPager;
    String vendorName, contactNo, collegeName;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar toolbar;
    private ListView drawerListView;
    private String[] drawerListViewItems;
    private List<String> dataItems;
    private List<String> mType;
    private delivery.santa.delivery.customer.JSONClasses ob,ob1,ob2;
    public static TextView tvf;
    private ProgressDialog pDialog;
    delivery.santa.delivery.customer.DB db;
    delivery.santa.delivery.customer.SessionManager session;
    private TextView t1,t2;
    Drawable bmp1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.abc_primary_text_disable_only_material_light));
        }


        Bundle bundle=getIntent().getExtras();
        vendorName=bundle.getString("vendorName");
        collegeName=bundle.getString("collegeName");
        setTitle(vendorName);
        contactNo = bundle.getString("contact");

        byte[] b = bundle.getByteArray("vendorImage");

        final Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
        bmp1 = new BitmapDrawable(getResources(), bmp);


        db = new delivery.santa.delivery.customer.DB(getApplicationContext());

        mViewPager = (MaterialViewPager) findViewById(R.id.materialViewPager);

        t1=(TextView)findViewById(R.id.txtUsername);
        t2=(TextView)findViewById(R.id.txtUserEmail);

        session = new delivery.santa.delivery.customer.SessionManager(getApplicationContext());
        if(session.isLoggedIn())
        {
            ob1= delivery.santa.delivery.customer.AppController.getList(getApplicationContext(), "name");
            ob2= delivery.santa.delivery.customer.AppController.getList(getApplicationContext(), "email");
            t1.setText(ob1.name);
            t2.setText(ob2.email);
        }

        drawerListViewItems= new String[]{"My Orders", "College", "T&C", "Privacy Policy", "About Us", "Log Out"};
        drawerListView = (ListView) findViewById(R.id.leftdrawer);
        List<String> dataTemp = Arrays.asList(drawerListViewItems);
        dataItems = new ArrayList<>();
        dataItems.addAll(dataTemp);
        delivery.santa.delivery.customer.DrawerAdapter adapter=new delivery.santa.delivery.customer.DrawerAdapter(this, (ArrayList<String>) dataItems, "1", b, vendorName, collegeName, contactNo);
        drawerListView.setAdapter(adapter);

        toolbar = mViewPager.getToolbar();
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (toolbar != null)
        {
            setSupportActionBar(toolbar);

            final ActionBar actionBar = getSupportActionBar();
            if (actionBar != null)
            {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowHomeEnabled(true);
                actionBar.setDisplayShowTitleEnabled(true);
                actionBar.setDisplayUseLogoEnabled(false);
                actionBar.setHomeButtonEnabled(true);
                //actionBar.setSlidingActionBarEnabled(true);
            }
        }

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, 0, 0);
        mDrawer.setDrawerListener(mDrawerToggle);

        drawerListView.setOnItemClickListener(new DrawerItemClickListener());

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        ob = delivery.santa.delivery.customer.AppController.getList(getApplicationContext(), "type");
        mType = ob.type;

        mViewPager.getViewPager().setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                String type;

                switch (position % mType.size()) {
                    case 0:
                        type = mType.get(0);
                        return delivery.santa.delivery.customer.fragment.VendorFragment.newInstance(vendorName, type);
                    case 1:
                        type = mType.get(1);
                        return VendorFragment1.newInstance(vendorName, type);
                    case 2:
                        type = mType.get(2);
                        return VendorFragment2.newInstance(vendorName, type);
                    case 3:
                        type = mType.get(3);
                        return VendorFragment3.newInstance(vendorName, type);
                    case 4:
                        type = mType.get(4);
                        return VendorFragment4.newInstance(vendorName, type);
                    case 5:
                        type = mType.get(5);
                        return VendorFragment5.newInstance(vendorName, type);
                    case 6:
                        type = mType.get(6);
                        return VendorFragment6.newInstance(vendorName, type);
                    case 7:
                        type = mType.get(7);
                        return VendorFragment7.newInstance(vendorName, type);
                    case 8:
                        type = mType.get(8);
                        return VendorFragment8.newInstance(vendorName, type);
                    case 9:
                        type = mType.get(9);
                        return VendorFragment9.newInstance(vendorName, type);
                    default:
                        return delivery.santa.delivery.customer.fragment.VendorFragment.newInstance(vendorName, mType.get(position));
                }
            }

            @Override
            public int getCount() {
                return mType.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mType.get(position);
            }
        });

        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener()
        {
            @Override
            public HeaderDesign getHeaderDesign(int page)
            {
                switch (page)
                {
                    default:
                       return HeaderDesign.fromColorResAndDrawable(R.color.sendotp_medium_grey, bmp1);
                }

        //     return null;
            }
        });


        mViewPager.getViewPager().setOffscreenPageLimit(mViewPager.getViewPager().getAdapter().getCount());
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.cart:
                Intent intent = new Intent(this, delivery.santa.delivery.customer.CartActivity.class);
                intent.putExtra("vendor", vendorName);
                intent.putExtra("contact", contactNo);
                intent.putExtra("collegeName", collegeName);
                startActivity(intent);
                return true;
            default:
                return  mDrawerToggle.onOptionsItemSelected(item) /*|| super.onOptionsItemSelected(item)*/;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.cart);
        MenuItemCompat.setActionView(item, R.layout.cart_icon);

        RelativeLayout notifCount = (RelativeLayout) MenuItemCompat.getActionView(item);
        TextView tv = (TextView) notifCount.
                findViewById(R.id.hotlist_hot);
        int q=0;
        Cursor cursor1=this.db.getAllItems();
        cursor1.moveToFirst();
        if (cursor1.moveToFirst()) {
            do {
                q = q + Integer.parseInt(cursor1.getString(cursor1.getColumnIndex("qty")));
            } while (cursor1.moveToNext());
        }
        tv.setText(String.valueOf(q));
        this.tvf=tv;
        item.getActionView().setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                int q=0;
                Cursor cursor1=db.getAllItems();
                cursor1.moveToFirst();
                if (cursor1.moveToFirst()) {
                    do {
                        q = q + Integer.parseInt(cursor1.getString(cursor1.getColumnIndex("qty")));
                    } while (cursor1.moveToNext());
                }

                if (q==0) {
                    Toast.makeText(v.getContext(),"Cart is empty.",Toast.LENGTH_LONG).show();
                }

                else
                {
                    Intent intent = new Intent(v.getContext(), delivery.santa.delivery.customer.CartActivity.class);
                    intent.putExtra("vname", vendorName);
                    intent.putExtra("contact", contactNo);
                    intent.putExtra("collegeName", collegeName);
                    startActivity(intent);
                }
            }
        });

        return true;
    }


    private class DrawerItemClickListener implements ListView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id)
        {

        }
    }

    @Override
    public void onBackPressed()
    {
        if(mDrawer.isDrawerOpen(Gravity.LEFT)){
            mDrawer.closeDrawer(Gravity.LEFT);
        }

        else if(db.getAllItems().moveToFirst())
        {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Exit")
                    .setMessage("Are you sure you want to clear the cart?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            db.deleteAllItems();
                            supportFinishAfterTransition();
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
        }

        else super.onBackPressed();
    }

    public void onResume()
    {
        session = new delivery.santa.delivery.customer.SessionManager(getApplicationContext());

        /*if (session.isLoggedIn()) {
            t1.setText("signed in");
        }

        else t1.setText("signup");
*/

        super.onResume();
    }
}
