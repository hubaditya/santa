	package com.androidbelieve.drawerwithswipetabs;

import android.support.v7.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolders> {

    private List<List<Component>> temp;
    private List<String> itemList;
    private List<String> cityList;
    private List<String> idList;
    private List<String> clgList,mVenCon;
    private List<String> venList,mInstr;
    private List<String> cusList;
    private String token;
    private Context context;
    ComponentAdapter adapter;
    private List<String> amount;
    private List<String> tod,stats;

    private final LayoutInflater mInflater;

    private List<Component> my_list;
    private ProgressDialog progressDialog;

    public RecyclerViewAdapter(Context context, List<String> itemList, List<List<Component>> temp, ArrayList<String> mCity,
                               ArrayList<String> mCollege, ArrayList<String> mVendor, ArrayList<String> mCusName,
                               ArrayList<String> mIdName, String token, ArrayList<String> amount, ArrayList<String> tod,
                               ArrayList<String> mInstr, ArrayList<String> mVenCon, ArrayList<String> mStatus)
    {
        this.itemList = itemList;
        mInflater = LayoutInflater.from(context);
        this.cityList = mCity;
        this.mVenCon=mVenCon;
        this.mInstr=mInstr;
        this.clgList = mCollege;
        this.venList = mVendor;
        this.cusList = mCusName;
        this.idList=mIdName;
        this.stats=mStatus;
        this.token=token;
        this.context = context;
        this.temp = temp;
        this.amount=amount;
        this.tod=tod;
    }

    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = mInflater.inflate(R.layout.order_card, null);
        RecyclerViewHolders rcv = new RecyclerViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolders holder, final int position) {
        holder.orderID.setText(itemList.get(position));
        holder.totAmt.setText(amount.get(position));
        holder.timeofdel.setText(tod.get(position));
        final String idd=idList.get(position);
        holder.stat=0;
        my_list = temp.get(position);

        adapter = new ComponentAdapter(context, R.layout.dialog_list_item, my_list);
        holder.dishList.setAdapter(adapter);

        ViewGroup.LayoutParams params = holder.dishList.getLayoutParams();
        params.height = adapter.getOrderSize() * params.height;
        holder.dishList.setLayoutParams(params);
        holder.dishList.requestLayout();*/

public class RecyclerViewAdapter extends ExpandableRecyclerAdapter<RecyclerViewHolders, ComponentAdapter.ComponentHolder>
{
    private LayoutInflater mInflator;
    List<? extends ParentListItem> parentItemList;
    Context context;
    private ProgressDialog progressDialog;
    ModelOrder modelOrder;

    public RecyclerViewAdapter(Context context, List<? extends ParentListItem> parentItemList)
    {
        super(parentItemList);
        this.context = context;
        mInflator = LayoutInflater.from(context);
        this.parentItemList=parentItemList;
    }

    @Override
    public RecyclerViewHolders onCreateParentViewHolder(ViewGroup parentViewGroup)
    {
        View recipeView = mInflator.inflate(R.layout.order_card, parentViewGroup, false);
        return new RecyclerViewHolders(recipeView);
    }

    @Override
    public void onBindChildViewHolder(ComponentAdapter.ComponentHolder holder, int position, Object childListItem)
    {
        Component cp = (Component) childListItem;
        holder.name.setText(cp.getName());
        holder.qty.setText(cp.getQty());

        holder.name.setTag(position);
        holder.qty.setTag(position);
    }

    @Override
    public ComponentAdapter.ComponentHolder onCreateChildViewHolder(ViewGroup childViewGroup) {
        View ingredientView = mInflator.inflate(R.layout.dialog_list_item, childViewGroup, false);
        return new ComponentAdapter.ComponentHolder(ingredientView);
    }

    @Override
    public void onBindParentViewHolder(final RecyclerViewHolders holder, final int position, final ParentListItem parentListItem)
    {
        modelOrder = (ModelOrder) parentListItem;

        holder.orderID.setText(modelOrder.mOrders);
        holder.totAmt.setText(modelOrder.amount);
        holder.timeofdel.setText(modelOrder.tod);

        holder.city = modelOrder.mCity;
        holder.college = modelOrder.mCollege;
        holder.vendor = modelOrder.mVendor;
        holder.customer = modelOrder.mCusName;
        holder.contact = modelOrder.mUserCon;
        holder.vContact = modelOrder.mVenCon;
        holder.instruct = modelOrder.mInstr;
        holder.status = modelOrder.mStatus;

        holder.orderID.setBackgroundColor(Color.WHITE);

        holder.instr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(context);
                    alert.setTitle("SPECIAL INSTRUCTIONS"); //Set Alert dialog title here
                    alert.setMessage(holder.instruct); //Message here
                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                        }
                    });

                    alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                        }
                    });
                    android.app.AlertDialog alertDialog = alert.create();
                    alertDialog.show();
                }
            }
        });


        if(holder.status.equals("0"))
        {
            holder.orderID.setBackgroundColor(Color.GREEN);
            holder.orderID.setClickable(false);
            holder.instr.setClickable(false);
        }

        else if(holder.status.equals("-1"))
        {
            holder.orderID.setBackgroundColor(Color.YELLOW);
            holder.orderID.setClickable(false);
            holder.instr.setClickable(false);
        }

        else if(holder.status.equals("-3"))
        {
            holder.orderID.setBackgroundColor(Color.RED);
            holder.orderID.setClickable(false);
            holder.instr.setClickable(false);
        }

        else
        {
            holder.orderID.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(final View v)
                {
                    LayoutInflater li = LayoutInflater.from(v.getContext());
                    View promptsView = li.inflate(R.layout.order_customer_details_dialog, null);

                    TextView tcity = (TextView) promptsView.findViewById(R.id.textView2);
                    TextView tcollege = (TextView) promptsView.findViewById(R.id.textView10);
                    TextView tvendor = (TextView) promptsView.findViewById(R.id.textView11);
                    TextView tcusname = (TextView) promptsView.findViewById(R.id.textView12);

                    tcity.setText(holder.city);
                    tcollege.setText(holder.college);
                    tvendor.setText(holder.vendor);
                    tcusname.setText(holder.customer);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setView(promptsView);
                    alertDialogBuilder.setTitle(holder.orderID.getText().toString()); //Set Alert dialog title here

                    alertDialogBuilder.setPositiveButton("ACCEPT", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int whichButton)
                        {
                            holder.orderID.setBackgroundColor(Color.YELLOW);
                            holder.orderID.setClickable(false);
                            modelOrder.updateStatus(parentListItem, "-1");
                            progressDialog = new ProgressDialog(v.getContext());
                            progressDialog.setCancelable(false);
                            acceptOrder(holder.orderID.getText().toString().substring(9), "-1", holder.contact, holder.vContact);
                        }
                    });

                    alertDialogBuilder.setNegativeButton("REJECT", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int whichButton)
                        {
                            holder.orderID.setBackgroundColor(Color.RED);
                            holder.orderID.setClickable(false);
                            modelOrder.updateStatus(parentListItem, "-3");
                            progressDialog = new ProgressDialog(v.getContext());
                            progressDialog.setCancelable(false);
                            rejectOrder(holder.orderID.getText().toString().substring(9), "-3", holder.contact, holder.vContact);
                            dialog.cancel();
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            });

        }

        holder.orderID.setTag(position);
        holder.instr.setTag(position);
        holder.timeofdel.setTag(position);
        holder.totAmt.setTag(position);
    }

    public void acceptOrder(final String orderID, final String status ,final String contact, final String vContact)
    {
        String tag_string_req = "req_accept";

        progressDialog.setMessage("Loading ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_STATUS, new Response.Listener<String>()
        {

            @Override
            public void onResponse(String response)
            {
                hideDialog();

                try {
                }
                catch (Exception e) {
                    Toast.makeText(context, "exception error", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(context,"volley error"+ error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("tag", "accept");
                params.put("token", modelOrder.token);
                params.put("orderid", orderID);
                params.put("status", status);
                params.put("userContactNo",contact);
                params.put("contactNo", vContact);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void rejectOrder(final String orderID, final String status, final String contact, final String vContact)
    {
        String tag_string_req = "req_reject";

        progressDialog.setMessage("Loading ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_STATUS, new Response.Listener<String>()
        {

            @Override
            public void onResponse(String response)
            {
                hideDialog();

                try {
                }

                catch (Exception e) {
                    Toast.makeText(context,"exception error", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"volley error"+ error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("tag", "reject");
                params.put("token", modelOrder.token);
                params.put("orderid", orderID);
                params.put("status", status);
                params.put("userContactNo",contact);
                params.put("contactNo", vContact);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void hideDialog()
    {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    private void showDialog()
    {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }


    /*@Override
    public int getItemCount() {
        return this.itemList.size();
    }*/
}