package com.androidbelieve.drawerwithswipetabs;

/**
 * Created by utsav on 12/9/2015.
 */

public class Component {

    private String name;
    private String qty;

    public Component(String name, String qty) {
        // TODO Auto-generated constructor stub
        this.name=name;
        this.qty=qty;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getQty() {
        return qty;
    }
    public void setQty(String qty) {
        this.qty = qty;
    }

}
