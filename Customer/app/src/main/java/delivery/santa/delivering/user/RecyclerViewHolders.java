package delivery.santa.delivery.customer;

import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.github.florent37.materialviewpager.sample.R;

public class RecyclerViewHolders extends ParentViewHolder implements View.OnClickListener
{
    public TextView orderID,status,tot,date;

    public RecyclerViewHolders(View itemView)
    {
        super(itemView);
        itemView.setOnClickListener(this);
        orderID = (TextView)itemView.findViewById(R.id.textView);
        status = (TextView)itemView.findViewById(R.id.textView13);
//        dishList=(RecyclerView)itemView.findViewById(R.id.listView2);
        date = (TextView)itemView.findViewById(R.id.textView14);
        tot=(TextView)itemView.findViewById(R.id.textView4);

    }

    @Override
    public void onClick(View view) {
        //Toast.makeText(view.getContext(), "Clicked ORDER = " + getPosition(), Toast.LENGTH_SHORT).show();
    }
}