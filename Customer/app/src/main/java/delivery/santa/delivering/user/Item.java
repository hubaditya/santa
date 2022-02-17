package delivery.santa.delivery.customer;

public class Item
{
    String name;
    String price;
    String qty;
    String amount;
    String vendor;

    public Item(String name, String price, String qty, String vendorName)
    {
        this.name=name;
        this.price=price;
        this.qty=qty;
        vendor=vendorName;
        //this.amount=amount;
        int a=Integer.parseInt(price);
        int b=Integer.parseInt(qty);
        this.amount= String.valueOf(a*b);
    }

    public String getName()
    { return this.name; }

    public String getPrice()
    { return this.price; }

    public String getQuantity()
    { return this.qty; }

    public String getAmount()
    { return this.amount; }

    public String getVendor()
    { return this.vendor; }
}