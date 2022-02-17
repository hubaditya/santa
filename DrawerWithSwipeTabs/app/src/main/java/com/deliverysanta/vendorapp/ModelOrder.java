package com.deliverysanta.vendorapp;

import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.ArrayList;
import java.util.List;

public class ModelOrder implements ParentListItem
{
    public List<ModelOrder> mIngredients;

    String mOrders,mCode,mStatus;
    String mCity;
    String mCollege;
    String mInstr;
    String mVendor,mUserCon;
    String mCusName;
    String mIdName;
    String amount;
    String tod;
    List<Component> temp;
    String token;

    public ModelOrder initialize(String mOrders, List<Component> temp,
                                 String mCity, String mCollege, String mVendor,
                                 String mCusName,String mIdName, String token,
                                 String amount, String tod, String mInstr,
                                 String mStatus, String mUserCon, String mCode)
    {
        ModelOrder modelOrder = new ModelOrder();

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
        modelOrder.mStatus=mStatus;
        modelOrder.mUserCon=mUserCon;
        modelOrder.mCode=mCode;

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

