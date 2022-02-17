package com.androidbelieve.drawerwithswipetabs;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.ArrayList;
import java.util.List;

public class ModelOrder implements ParentListItem
{
    public List<ModelOrder> mIngredients;

    String mOrders,mStatus;
    String mCity;
    String mCollege;
    String mInstr;
    String mVendor, mUserCon, mVenCon;
    String mCusName;
    String mIdName;
    String amount;
    String tod;
    List<Component> temp;
    String token;
    ModelOrder modelOrder;

    public ModelOrder initialize(String mOrders, List<Component> temp,
                                 String mCity, String mCollege, String mVendor,
                                 String mCusName,String mIdName, String token,
                                 String amount, String tod, String mInstr,
                                 String mUserCon, String mStatus, String mVenCon)
    {
        modelOrder = new ModelOrder();

        modelOrder.mOrders=mOrders;
        modelOrder.temp=temp;
        modelOrder.mCity=mCity;
        modelOrder.mCollege=mCollege;
        modelOrder.mVendor=mVendor;
        modelOrder.mCusName=mCusName;
        modelOrder.mIdName=mIdName;
        modelOrder.mOrders=mOrders;
        modelOrder.token=token;
        modelOrder.amount=amount;
        modelOrder.tod=tod;
        modelOrder.mInstr=mInstr;
        modelOrder.mUserCon=mUserCon;
        modelOrder.mVenCon=mVenCon;
        modelOrder.mStatus=mStatus;

        return  modelOrder;
    }
    public ModelOrder () {
    }

    public ModelOrder(ArrayList<ModelOrder> modelOrderArrayList) {
        mIngredients = modelOrderArrayList;
    }

    public void updateStatus(ParentListItem parentListItem, String status)
    {
        ModelOrder modelOrder = (ModelOrder) parentListItem;
        modelOrder.mStatus = status;
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
