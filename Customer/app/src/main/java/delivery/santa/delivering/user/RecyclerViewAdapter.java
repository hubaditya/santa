package delivery.santa.delivery.customer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;
import com.github.florent37.materialviewpager.sample.R;

import java.util.List;

public class RecyclerViewAdapter extends ExpandableRecyclerAdapter<RecyclerViewHolders, ComponentAdapter.ComponentHolder> {

    private LayoutInflater mInflator;
    List<? extends ParentListItem> parentItemList;

    public RecyclerViewAdapter(Context context,List<? extends ParentListItem> parentItemList) {
        super(parentItemList);
        mInflator = LayoutInflater.from(context);
        this.parentItemList=parentItemList;
    }

    @Override
    public RecyclerViewHolders onCreateParentViewHolder(ViewGroup parentViewGroup) {
        View recipeView = mInflator.inflate(R.layout.my_order_card, parentViewGroup, false);
        return new RecyclerViewHolders(recipeView);
    }

    @Override
    public ComponentAdapter.ComponentHolder onCreateChildViewHolder(ViewGroup childViewGroup) {
        View ingredientView = mInflator.inflate(R.layout.my_order_card_list_item, childViewGroup, false);
        return new ComponentAdapter.ComponentHolder(ingredientView);
    }

    @Override
    public void onBindParentViewHolder(RecyclerViewHolders holder, int position, ParentListItem parentListItem)
    {
        ModelOrder modelOrder = (ModelOrder) parentListItem;
        holder.orderID.setText(modelOrder.itemList);
        holder.tot.setText(modelOrder.tot);
        holder.date.setText(modelOrder.date);
        holder.status.setText(modelOrder.statList);

    }

    @Override
    public void onBindChildViewHolder(ComponentAdapter.ComponentHolder holder, int position, Object childListItem) {
        Component cp = (Component) childListItem;
        holder.name.setText(cp.getName());
        holder.qty.setText(cp.getQty());

        holder.name.setTag(position);
        holder.qty.setTag(position);
    }
}