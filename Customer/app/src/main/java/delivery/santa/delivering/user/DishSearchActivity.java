package delivery.santa.delivery.customer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.materialviewpager.sample.R;

import java.util.ArrayList;
import java.util.List;

public class DishSearchActivity extends Fragment implements SearchView.OnQueryTextListener
{
    private RecyclerView mRecyclerView;
    private ExampleAdapter mAdapter;
    private static List<ExampleModel> mName;
    private static List<ExamplePrice> mPrice;
    private static List<ExampleCategory> mCat;

    private DB db;
    static String vendorName;

    public static DishSearchActivity newInstance(String vendorname, String[] dish, String[] rate, String[] cat)
    {
        vendorName = vendorname;
        mName = new ArrayList<>();
        mPrice = new ArrayList<>();
        mCat = new ArrayList<>();

        for(String x : dish)
            mName.add(new ExampleModel(x));

        for(String y : rate)
            mPrice.add(new ExamplePrice(y));

        for(String z : cat)
            mCat.add(new ExampleCategory(z));

        return new DishSearchActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final View view = inflater.inflate(R.layout.activity_dish_search, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.listView2);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        db=new DB(getActivity());
        mAdapter = new ExampleAdapter(this.getContext(), db, mName, mPrice, vendorName, mCat);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.menu_dish_search, menu);
        final MenuItem item = menu.findItem(R.id.search1);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextChange(String query)
    {
        final List[] filteredModelList = filter(mName, mPrice, mCat, query);
        mAdapter.animateTo(filteredModelList);
        mRecyclerView.scrollToPosition(0);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private List[] filter(List<ExampleModel> names, List<ExamplePrice> prices, List<ExampleCategory> mCat, String query)
    {
        query = query.toLowerCase();

        final List<ExampleModel> filteredModelList1 = new ArrayList<>();
        final List<ExamplePrice> filteredModelList2 = new ArrayList<>();
        final List<ExampleCategory> filteredModelList3 = new ArrayList<>();

        for (ExampleModel name : names)
        {
            final String text = name.getText().toLowerCase();

            if (text.contains(query))
            {
                ExamplePrice price = prices.get(names.indexOf(name));
                ExampleCategory cat = mCat.get(names.indexOf(name));
                filteredModelList1.add(name);
                filteredModelList2.add(price);
                filteredModelList3.add(cat);
            }
        }

        return new List[] { filteredModelList1,filteredModelList2, filteredModelList3 } ;
    }
}
