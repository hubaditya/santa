package delivery.santa.delivery.customer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.florent37.materialviewpager.sample.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VendorsAdapter extends RecyclerView.Adapter<VendorsAdapter.ViewHolder>
{
    private List<String> dataSource; //vendor names
    private List<String> statSource; //vendor status
    private List<String> contactSource; //vendor contact
    private static String collegeName;

    public VendorsAdapter(ArrayList dataList, ArrayList statList, ArrayList contactList, String collegeName)
    {
        this.dataSource = dataList;
        this.statSource = statList;
        this.contactSource = contactList;
        this.collegeName = collegeName;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendor_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        holder.textView.setText(dataSource.get(position));
        holder.textView2.setText(statSource.get(position));
        {
            if(dataSource.get(position).equalsIgnoreCase("TADKA"))
                holder.imageView.setBackgroundResource(R.drawable.tadka);
            if(dataSource.get(position).equalsIgnoreCase("DOMINOS"))
                holder.imageView.setBackgroundResource(R.drawable.dominos);
            if(dataSource.get(position).equalsIgnoreCase("CAKES n COOKIES"))
                holder.imageView.setBackgroundResource(R.drawable.cakesncookies);
            if(dataSource.get(position).equalsIgnoreCase("THE BURGER STREET"))
                holder.imageView.setBackgroundResource(R.drawable.burgerstreet);
            if(dataSource.get(position).equalsIgnoreCase("PIZZA HUT"))
                holder.imageView.setBackgroundResource(R.drawable.pizzahut);
            if(dataSource.get(position).equalsIgnoreCase("AMRITSARI CHOLE KULCHE"))
                holder.imageView.setBackgroundResource(R.drawable.amritsari);
            if(dataSource.get(position).equalsIgnoreCase("BHATIYA PANEER WALA"))
                holder.imageView.setBackgroundResource(R.drawable.bhatiya);
            if(dataSource.get(position).equalsIgnoreCase("KEBABS AND CURRIES"))
                holder.imageView.setBackgroundResource(R.drawable.kebabsncurries);
        }

        if(position==0)
        {
            // holder.textView.setBackground(context.getDrawable(R.drawable.wallpaper));
        }
        holder.imageView.setTag(position);
        holder.textView.setTag(position);
        holder.textView2.setTag(position);
        holder.conno=contactSource.get(position);
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        protected TextView textView,textView2;
        protected ImageView imageView;
        protected String conno;

        private ProgressDialog pDialog;
        JSONArray jsonArray;
        JSONClasses obj;

        public ViewHolder(View itemView)
        {
            super(itemView);
            textView =  (TextView) itemView.findViewById(R.id.vendorName);
            textView2 =  (TextView) itemView.findViewById(R.id.textView22);
            imageView = (ImageView)itemView.findViewById(R.id.imageView2);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(textView2.getText().toString().equals("Available")){

                        pDialog = new ProgressDialog(v.getContext());
                        pDialog.setCancelable(false);
                        findType(textView.getText().toString());
                    }
                    else {
                        Snackbar.make(v, "Vendor unavailable currently.", Snackbar.LENGTH_SHORT).show();
                    }
                }
            });
        }

        private void findType(final String nameVendor)
        {
            obj = new JSONClasses();
            obj.type = new ArrayList<>();

            String tag_string_req = "req_type";

            pDialog.setMessage("Loading ...");
            showDialog();

            StringRequest strReq = new StringRequest(Request.Method.POST,
                    AppConfig.URL_TYPE, new Response.Listener<String>()
            {

                @Override
                public void onResponse(String response)
                {
                    hideDialog();

                    try
                    {
                        jsonArray = new JSONArray(response);

                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            obj.type.add(jsonObject.getString("typeOfDish"));
                        }

                        AppController.setList(itemView.getContext(), "type", obj);

                        Intent intent = new Intent(itemView.getContext(), MainActivity.class);
                        intent.putExtra("vendorName", textView.getText().toString());
                        intent.putExtra("contact", conno);
                        intent.putExtra("collegeName", collegeName);
                        imageView.buildDrawingCache();
                        Bitmap bitmap = imageView.getDrawingCache();
                        //    Bitmap bitmap = test.getBitmap();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 0, baos);
                        byte[] b = baos.toByteArray();

                        intent.putExtra("vendorImage",b);
                        itemView.getContext().startActivity(intent);
                    }
                    catch (JSONException e) {
                        Toast.makeText(itemView.getContext(), "No or slow Internet connection. Please try again later. ", Toast.LENGTH_LONG).show();
                    }

                }
            }, new Response.ErrorListener()
            {

                @Override
                public void onErrorResponse(VolleyError error)
                {
                    Toast.makeText(itemView.getContext(), "No or slow Internet connection. Please try again later. ", Toast.LENGTH_LONG).show();

                    hideDialog();
                }
            })

            {
                @Override
                protected Map<String, String> getParams()
                {
                    Map<String, String> params = new HashMap<>();
                    params.put("tag", "dishprice");
                    params.put("restaurant", nameVendor);

                    return params;
                }

            };

            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }

        private void showDialog()
        {
            if (!pDialog.isShowing())
                pDialog.show();
        }

        private void hideDialog()
        {
            if (pDialog.isShowing())
                pDialog.dismiss();
        }

    }
}