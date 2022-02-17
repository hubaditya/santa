package delivery.santa.delivery.customer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.florent37.materialviewpager.sample.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DrawerAdapter extends BaseAdapter
{

    private final Context context;
    private LayoutInflater inflater;
    private ArrayList<String> data = new ArrayList<>();
    DB db;
    private SessionManager session;
    JSONClasses ob;
    String token, check, vendor, college, contact;
    byte b[];

    public DrawerAdapter(Context context, ArrayList<String> dataItem, String check, byte b[], String vendor, String college, String contact)
    {
        this.context=context;
        this.data = dataItem;
        db = new DB(context);
        this.check = check;
        this.b = b;
        this.vendor = vendor;
        this.college = college;
        this.contact = contact;
        session = new SessionManager(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder2 holder = null;

        if (convertView == null)
        {
            holder = new ViewHolder2();
            inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.drawer_list_item, null);

            holder.tv1 = (TextView) convertView.findViewById(R.id.navItem); //Name

            convertView.setTag(holder);

        }

        else holder = (ViewHolder2) convertView.getTag();

        if(!session.isLoggedIn() && data.get(position).equals("Log Out")) {
            holder.tv1.setVisibility(View.INVISIBLE);
        }

        else holder.tv1.setText(data.get(position));

        final String sel=data.get(position);

        if(data.get(position).equals("My Orders")){
            holder.tv1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_person_black_36dp, 0, 0, 0);
        }
        if(data.get(position).equals("College")){
            holder.tv1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_school_black_36dp, 0, 0, 0);
        }
        if(data.get(position).equals("T&C")){
            holder.tv1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_info_black_36dp, 0, 0, 0);
        }
        if(data.get(position).equals("Privacy Policy")){
            holder.tv1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_security_black_36dp, 0, 0, 0);
        }
        if(data.get(position).equals("About Us")){
            holder.tv1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_business_center_black_36dp, 0, 0, 0);
        }
        if(data.get(position).equals("Log Out")){
            holder.tv1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_power_settings_new_black_36dp, 0, 0, 0);
        }

        if(session.isLoggedIn())
        {
            ob=AppController.getList(context,"token");
            token=ob.token;
        }

        holder.tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sel.equals("About Us")) {
                /*    det = "about us";
                    Intent i = new Intent(v.getContext(), AboutUsActivity.class);
                    i.putExtra("data", det);
                    context.startActivity(i);
                */
                    Toast.makeText(v.getContext(),"Soon to be updated",Toast.LENGTH_LONG).show();
                }

                if (sel.equals("T&C")) {
/*                    det = "terms and conditions";
                    Intent i = new Intent(v.getContext(), AboutUsActivity.class);
                    i.putExtra("data", det);
                    context.startActivity(i);*/

                    Toast.makeText(v.getContext(),"Soon to be updated",Toast.LENGTH_LONG).show();
                }

                if (sel.equals("Privacy Policy")) {
/*                    det = "privacy policy";
                    Intent i = new Intent(v.getContext(), AboutUsActivity.class);
                    i.putExtra("data", det);
                    context.startActivity(i);*/

                    Toast.makeText(v.getContext(),"Soon to be updated",Toast.LENGTH_LONG).show();
                }

                if (sel.equals("College"))
                {
                    if (session.isLoggedIn())
                    {
                        session.setLogin(false);
                        db.deleteAllItems();
                        Toast.makeText(v.getContext(), "You have been logged out.", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(v.getContext(), CollegeActivity.class);
                        context.startActivity(i);
                        ((Activity)context).finish();
                    }
                    else
                    {
                        Intent i = new Intent(v.getContext(), CollegeActivity.class);
                        context.startActivity(i);
                        ((Activity) context).finish();
                    }
                }

                if (sel.equals("My Orders"))
                {
                    if(session.isLoggedIn())
                        checkOrder(token);

                    else
                    {
                        Intent i = new Intent(context, MyOrdersActivity.class);
                        context.startActivity(i);
                        ((Activity) context).finish();
                    }

                }

                if(session.isLoggedIn())
                {
                    if(sel.equals("Log Out"))
                    {
                        session.setLogin(false);
                        db.deleteAllItems();
                        Intent i = new Intent(v.getContext(), CollegeActivity.class);
                        context.startActivity(i);
                        ((Activity)context).finish();
                    }
                }

                /*if (sel.equals("Log Out")) {
                    if (session.isLoggedIn())
                    {
                        session.setLogin(false);
                        db.deleteAllItems();
                        Intent i = new Intent(v.getContext(), CollegeActivity.class);
                        context.startActivity(i);
                        ((Activity)context).finish();
                    }

                    else
                        Toast.makeText(v.getContext(), "You are already logged out.", Toast.LENGTH_LONG).show();
                }*/
            }
        });

        return convertView;
    }

    public void checkOrder(final String token)
    {
        String tag_string_req = "req_vendor";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_MYORDERS, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    if (jsonArray.length() == 0) {
                        Toast.makeText(context, "You have no previous orders.", Toast.LENGTH_LONG).show();
                    }

                    else
                    {
                        Intent i = new Intent(context, MyOrdersActivity.class);
                        i.putExtra("check", check);
                        i.putExtra("vendorImage", b);
                        i.putExtra("vendorName", vendor);
                        i.putExtra("collegeName", college);
                        i.putExtra("contact", contact);
                        context.startActivity(i);
                        ((Activity) context).finish();
                    }
                }
                catch (JSONException e) {
                    Toast.makeText(context, "No or slow Internet connection. Please try again later. ", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "No or slow Internet connection. Please try again later. ", Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("tag", "myorders");
                params.put("token", token);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    class ViewHolder2 {
        TextView tv1;
    }
}