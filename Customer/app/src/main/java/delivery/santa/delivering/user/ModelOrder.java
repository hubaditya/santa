package delivery.santa.delivery.customer;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.ArrayList;
import java.util.List;

public class ModelOrder implements ParentListItem
{
    public List<ModelOrder> mIngredients = new ArrayList();
    public String itemList;
    public List<Component> temp;
    public  String tot;
    public String date;
    public String statList;
    public ModelOrder initialize(String itemList, List<Component> temp,
                      String tot, String date, String  statList)
    {
        ModelOrder modelOrder = new ModelOrder();
        modelOrder.itemList=itemList;
        modelOrder.temp=temp;
        modelOrder.tot=tot;
        modelOrder.date=date;
        modelOrder.statList=statList;
        return  modelOrder;
    }
    public ModelOrder ()
    {

    }
    public ModelOrder(ArrayList<ModelOrder> modelOrderArrayList)
    {
        mIngredients = modelOrderArrayList;
    }

    @Override
    public List getChildItemList() {
        return temp;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return true;
    }
}
