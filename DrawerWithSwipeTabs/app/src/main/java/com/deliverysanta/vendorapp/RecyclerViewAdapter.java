package com.deliverysanta.vendorapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolders>
{
    private List<List<Component>> temp;
    private List<String> itemList;
    private List<String> cityList;
    private List<String> idList;
    private List<String> clgList;
    private List<String> venList,mInstr;
    private List<String> cusList;
    private String token;
    private Context context;
    ComponentAdapter adapter;
    List<Component> my_list;
    private ProgressDialog progressDialog;
    private final LayoutInflater mInflater;
    private List<String> amount;
    private List<String> tod,stats,userCons, mCode;

    public RecyclerViewAdapter(Context context, List<String> itemList, List<List<Component>> temp, ArrayList<String> mCity,
                               ArrayList<String> mCollege, ArrayList<String> mVendor, ArrayList<String> mCusName,
                               ArrayList<String> mIdName, String token, ArrayList<String> amount, ArrayList<String> tod,
                               ArrayList<String> mInstr, ArrayList<String> mStatus, ArrayList<String> mUserCon, ArrayList<String> mCode)
    {
        this.itemList = itemList;
        mInflater = LayoutInflater.from(context);
        this.cityList = mCity;
        this.clgList = mCollege;
        this.venList = mVendor;
        this.cusList = mCusName;
        this.idList=mIdName;
        this.userCons=mUserCon;
        this.mInstr=mInstr;
        this.stats=mStatus;
        this.token=token;
        this.context = context;
        this.temp = temp;
        this.amount=amount;
        this.tod=tod;
        this.mCode = mCode;
    }

    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View layoutView = mInflater.inflate(R.layout.order_card, null);
        RecyclerViewHolders rcv = new RecyclerViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolders holder, final int position)
    {*/

public class RecyclerViewAdapter extends ExpandableRecyclerAdapter<RecyclerViewHolders, ComponentAdapter.ComponentHolder>
{
    private LayoutInflater mInflator;
    List<? extends ParentListItem> parentItemList;
    Context context;
    private ProgressDialog progressDialog;
    ModelOrder modelOrder;

    public RecyclerViewAdapter(Context context,List<? extends ParentListItem> parentItemList)
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

        holder.totAmt.setText(modelOrder.amount);
        holder.timeOfdel.setText(modelOrder.tod);

        final String idd = modelOrder.mIdName;
        String iddd = idd;
        holder.oId.setText(iddd);

        holder.userContact = modelOrder.mUserCon;
        holder.code = modelOrder.mCode;
        holder.instruct = modelOrder.mInstr;
        holder.orID = modelOrder.mOrders.substring(9);
        holder.status = modelOrder.mStatus;

        holder.orderID.setBackgroundColor(Color.WHITE);

        if (holder.status.equals("0"))
        {
            holder.orderID.setBackgroundColor(Color.GREEN);
            holder.orderID.setClickable(false);
            holder.instr.setClickable(true);
        }

        else if (holder.status.equals("-3"))
        {
            holder.orderID.setBackgroundColor(Color.RED);
            holder.orderID.setClickable(false);
            holder.instr.setClickable(false);
        }

        else
        {
            holder.orderID.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {

                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle(holder.oId.getText().toString());
                    alert.setMessage("Do you accept the order?");
                    alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            holder.orderID.setBackgroundColor(Color.GREEN);
                            holder.orderID.setClickable(false);
                            holder.instr.setClickable(true);
                            modelOrder.updateStatus(parentListItem, "0");
                            progressDialog = new ProgressDialog(v.getContext());
                            progressDialog.setCancelable(false);
                            acceptOrder(idd, "0", holder.orID, holder.userContact, holder.code);
                        }
                    });

                    alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            holder.orderID.setBackgroundColor(Color.RED);
                            holder.orderID.setClickable(false);
                            modelOrder.updateStatus(parentListItem, "-3");
                            progressDialog = new ProgressDialog(v.getContext());
                            progressDialog.setCancelable(false);
                            OrdersFragment.revenue = OrdersFragment.revenue - Integer.parseInt(holder.totAmt.getText().toString());
                            OrdersFragment.textView.setText(String.valueOf(OrdersFragment.revenue));
                            rejectOrder(idd, "-3", holder.orID, holder.userContact, holder.code);
                            dialog.cancel();
                        }
                    });
                    AlertDialog alertDialog = alert.create();
                    alertDialog.show();
                }
            });

        }

        if (holder.instruct.equals("")) {
            holder.instr.setVisibility(View.GONE);
        }
        {
            holder.instr.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    {
                        AlertDialog.Builder alert = new AlertDialog.Builder(context);
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
                        AlertDialog alertDialog = alert.create();
                        alertDialog.show();
                    }
                }
            });

/*
            holder.status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.stat == 1) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(context);
                        alert.setTitle(itemList.get(position)); //Set Alert dialog title here
                        alert.setMessage("Have you prepared the order?"); //Message here
                        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                holder.status.setBackgroundColor(Color.GREEN);
                                holder.status.setText("PREPARED");
                            }
                        });

                        alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                holder.status.setBackgroundColor(Color.RED);
                                holder.status.setText("UNPREPARED");
                                dialog.cancel();
                            }
                        });
                        AlertDialog alertDialog = alert.create();
                        alertDialog.show();
                    }
                }
            });
*/
        }
    }
/*
    private void assignboy(final String idd, final String tod)
    {
        String tag_string_req = "delboy_assign";
        progressDialog.setMessage("Loading ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_DELBOY, new Response.Listener<String>()
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
                params.put("tag", "assign");
                params.put("orderid",idd);
                params.put("timeOfDelivery", tod);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
*/

    public void acceptOrder(final String id, final String status, final String orID, final String contact, final String code)
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
                    Toast.makeText(context, "No or slow Internet connection. Please try again later.", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(context, "No or slow Internet connection. Please try again later.", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("tag", "accept");
                params.put("token", modelOrder.token);
                params.put("orderNo", id);
                params.put("orderid", orID);
                params.put("status", status);
                params.put("userContactNo",contact);
                params.put("uniqueCode", code);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void rejectOrder(final String id, final String status, final  String orID, final String contact, final String code)
    {
        String tag_string_req = "req_reject";
        progressDialog.setMessage("Loading ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_STATUS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response)
            {
                hideDialog();
                try {
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
                params.put("tag", "reject");
                params.put("token", modelOrder.token);
                params.put("orderNo", id);
                params.put("orderid", orID);
                params.put("status", status);
                params.put("userContactNo",contact);
                params.put("uniqueCode", code);

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