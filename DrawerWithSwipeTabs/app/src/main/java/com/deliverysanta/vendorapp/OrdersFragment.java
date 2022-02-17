package com.deliverysanta.vendorapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrdersFragment extends Fragment
{
	View rootview;
	Context context;
	private LinearLayoutManager lLayout;
	ArrayList<String> mOrders,mStatus;
	ArrayList<String> mCity,mUserCon;
	ArrayList<String> mCollege,mInstr;
	ArrayList<String> mVendor;
	ArrayList<String> mCusName;
	ArrayList<String> mIdName;
	List<Component> dishNameList;
	ArrayList<String> names;
	ArrayList<String> qtys;
	ArrayList<String> amount;
	ArrayList<String> tod;
	ArrayList<String> mCode;
	RecyclerViewAdapter rcAdapter;
	String tokenn;
	JSONClasses ob;
	public static TextView textView;
	public static int revenue;
	private ProgressDialog progressDialog;
	RecyclerView rView;
	List<List<Component>> temp;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		rootview=inflater.inflate(R.layout.orders_fragment, null);
		setHasOptionsMenu(true);
		lLayout=new LinearLayoutManager(rootview.getContext());
		rView = (RecyclerView)rootview.findViewById(R.id.ordersList);
		rView.setLayoutManager(lLayout);
		context=container.getContext();
		ob = AppController.getList(getActivity(), "token");
		String token = ob.token;
		tokenn=token;
		revenue=0;
		textView=(TextView)rootview.findViewById(R.id.textView18);
		progressDialog = new ProgressDialog(getActivity());
		progressDialog.setCancelable(false);
		getOrder(token);
		return rootview;
	}

	public void getOrder(final String token)
	{
		amount = new ArrayList<>();
		tod = new ArrayList<>();
		mUserCon = new ArrayList<>();
		mInstr = new ArrayList<>();
		mOrders = new ArrayList<>();
		mCity = new ArrayList<>();
		mCollege = new ArrayList<>();
		mVendor = new ArrayList<>();
		mIdName = new ArrayList<>();
		mCusName = new ArrayList<>();
		mStatus = new ArrayList<>();
		temp = new ArrayList<>();
		mCode = new ArrayList<>();

		revenue=0;

		String tag_string_req = "req_order";

		progressDialog.setMessage("Loading ...");
		showDialog();

		StringRequest strReq = new StringRequest(Request.Method.POST,
				AppConfig.URL_ORDER, new Response.Listener<String>()
		{

			@Override
			public void onResponse(String response)
			{
				hideDialog();
				try {
						JSONArray jsonArray = new JSONArray(response);

						for(int i = jsonArray.length()-1; i>=0; i--)
						{
							dishNameList = new ArrayList<>();
                            names = new ArrayList<>();
                            qtys = new ArrayList<>();

							JSONObject jsonObject = jsonArray.getJSONObject(i);

							String dish = jsonObject.getString("nameOfDish");
							String qty = jsonObject.getString("quantityOfDish");
							String sts = jsonObject.getString("status");
							String code = jsonObject.getString("uniqueCode");

							int le=0;

							for (String retdish: dish.split("--"))
								names.add(retdish);
							for (String retqty: qty.split("--"))
								qtys.add(retqty);
							for(le=0;le<names.size();le++)
								dishNameList.add(new Component(names.get(le), qtys.get(le)));

							temp.add(dishNameList);

							mOrders.add("OrderID: " + jsonObject.getString("orderid"));
							mCity.add("City: " + "JAIPUR");
							mIdName.add(jsonObject.getString("orderNo"));
							amount.add(jsonObject.getString("totalAmount"));
							revenue=revenue+Integer.parseInt(jsonObject.getString("totalAmount"));
							tod.add(jsonObject.getString("timeOfDelivery"));
							mCollege.add("College: " + jsonObject.getString("college"));
							mVendor.add("Vendor: " + jsonObject.getString("restaurant"));
							mCusName.add("Customer Name: " + jsonObject.getString("orderBy"));
							mStatus.add(sts);
							mUserCon.add(jsonObject.getString("userContactNo"));
							mInstr.add(jsonObject.getString("specialInstruction"));
							mCode.add(code);
						}

						textView.setText(String.valueOf(revenue));

					List<ModelOrder> modelOrderList = new ArrayList<>();

					for(int i=0;i<mOrders.size();i++)
					{
						ModelOrder temp2 = new ModelOrder().initialize(mOrders.get(i), temp.get(i),
								mCity.get(i), mCollege.get(i), mVendor.get(i), mCusName.get(i), mIdName.get(i),
								token, amount.get(i), tod.get(i), mInstr.get(i), mStatus.get(i), mUserCon.get(i), mCode.get(i));
						modelOrderList.add(temp2);
					}

					ModelOrder modelOrder = new ModelOrder((ArrayList<ModelOrder>) modelOrderList);
					rcAdapter = new RecyclerViewAdapter(rootview.getContext(), modelOrder.mIngredients);
					rView.setAdapter(rcAdapter);
					rcAdapter.notifyDataSetChanged();


					/*rcAdapter = new RecyclerViewAdapter(rootview.getContext(), mOrders, temp, mCity, mCollege, mVendor,
							mCusName, mIdName, token, amount, tod, mInstr, mStatus, mUserCon, mCode);
					rView.setAdapter(rcAdapter);
					rcAdapter.notifyDataSetChanged();*/
				}
				catch (Exception e) {
					Toast.makeText(context, "No or slow Internet connection. Please try again later.", Toast.LENGTH_LONG).show();
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(context, "No or slow Internet connection. Please try again later.", Toast.LENGTH_LONG).show();
				hideDialog();
			}
		}) {

			@Override
			protected Map<String, String> getParams()
			{
				Map<String, String> params = new HashMap<>();
				params.put("tag", "login");
				params.put("token", token);
				return params;
			}
		};
		AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
	}

	private void showDialog()
	{
		if (!progressDialog.isShowing())
			progressDialog.show();
	}

	private void hideDialog()
	{
		if (progressDialog.isShowing())
			progressDialog.dismiss();
	}


	public void onRefresh()
	{
		getOrder(tokenn);
		Snackbar.make(rootview, "List updated.", Snackbar.LENGTH_SHORT).show();
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		menu.findItem(R.id.action_refresh).setVisible(true);
		super.onPrepareOptionsMenu(menu);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_toolbar, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.action_refresh:
				onRefresh();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}

