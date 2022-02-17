package delivery.santa.delivery.customer;

/**
 * Created by delivering on 12/9/2015.
 */

public class Component {

    private String name;
//    private String price;
    private String qty;
  //  private String amt;

    public Component(String name, String qty) {
        // TODO Auto-generated constructor stub
        this.name=name;
    //    this.price=price;
        this.qty=qty;
      //  this.amt=amt;
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
/*

    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }

    public String getAmount() {
        return amt;
    }
    public void setAmount(String amt) {
        this.amt = amt;
    }

*/

}
