package com.delivery.santa.deliver;


public class AppConfig
{
    public static String URL = "http://ec2-52-35-231-110.us-west-2.compute.amazonaws.com:8080/api";
    public static String URL_LOGIN = URL + "/logindeliveryboy";
    public static String URL_VENDORS = URL + "/allresttoboy";
    public static String URL_ORDER = URL + "/allorderstoboyofrest";
    public static String URL_STATUS = URL + "/changestatusbyboy";
    public static String URL_BAGS = URL + "/addbags";
    public static String URL_GIVE = URL + "/allorderstoboy";
}
