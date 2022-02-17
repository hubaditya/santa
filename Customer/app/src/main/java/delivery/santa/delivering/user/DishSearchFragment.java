package delivery.santa.delivery.customer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.github.florent37.materialviewpager.sample.R;

public class DishSearchFragment extends AppCompatActivity
{
    String vendorName;
    String [] dish;
    String [] rate;
    String [] cat;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dishfraglayout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        vendorName=bundle.getString("vname");
        dish = bundle.getStringArray("dishList");
        rate = bundle.getStringArray("rate");
        cat = bundle.getStringArray("cat");

        if(savedInstanceState==null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.dishfraglayoutid,
                    DishSearchActivity.newInstance(vendorName, dish, rate, cat)).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
