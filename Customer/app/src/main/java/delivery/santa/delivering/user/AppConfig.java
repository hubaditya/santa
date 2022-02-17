package delivery.santa.delivery.customer;

public class AppConfig
{
    public static String URL = "http://ec2-52-35-231-110.us-west-2.compute.amazonaws.com:8080/api";
    public static String URL_COLLEGE = URL + "/allcollegename";
    public static String URL_VENDOR = URL + "/findrestbycollege";
    public static String URL_TYPE = URL + "/typeOfDish";
    public static String URL_MENU = URL + "/menubyrestnametype";
    public static String URL_FULLMENU = URL + "/menubyrestname";
    public static String URL_REGISTER = URL + "/signup";
    public static String URL_LOGIN = URL + "/login";
    public static String URL_ORDER = URL + "/addorder";
    public static String URL_MYORDERS = URL + "/userhistory";
    public static String URL_DELIVPRICE = URL + "/checkdeliveryprice";
    public static String URL_DELIVTIME = URL + "/deliverytime";
    public static String URL_VERIFY = URL + "/verifyotp";
    public static String URL_RESEND = URL + "/resendotp";
    public static String URL_ME = URL + "/me";
}