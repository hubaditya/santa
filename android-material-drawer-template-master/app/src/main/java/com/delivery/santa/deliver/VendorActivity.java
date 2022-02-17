package com.delivery.santa.deliver;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class VendorActivity extends AppCompatActivity
{
    ListView listview;
    List<String> dataItems;
    List<String> CusNames;
    List<String> Bills;
    List<String> code;
    List<String> contact;

    Button b1;
    TextView toPay;
    private Toolbar mToolbar;
    JSONClasses ob;

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor);

        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);

        listview=(ListView)findViewById(R.id.listView2);
        b1 = (Button)findViewById(R.id.button5);
        toPay=(TextView)findViewById(R.id.textView2);

        Bundle bundle = getIntent().getExtras();
        //final String vendor = bundle.getString("vendorName");
        int bill = bundle.getInt("bill");

        toPay.setText(String.valueOf(bill));

        ob = AppController.getList(VendorActivity.this, "id");
        dataItems = ob.id;
        CusNames = ob.cus;
        Bills = ob.bill;
        code = ob.code;
        contact = ob.con;

        VendorAdapter adapter=new VendorAdapter(this, (ArrayList<String>) dataItems,(ArrayList<String>) CusNames,(ArrayList<String>) Bills,
                (ArrayList<String>) code, (ArrayList<String>) contact);
        listview.setAdapter(adapter);

        b1.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(VendorActivity.this, Main2Activity.class);
                //i.putExtra("doneVendor", vendor);
                Main2Activity.act.finish();
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        menu.getItem(0).setVisible(false);
        menu.getItem(1).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

}
