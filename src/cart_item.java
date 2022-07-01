import java.io.Serializable;

public class cart_item implements Serializable{

    String prodid;
    String item_name;
    String item_price;
    String total_price;
    String qauntity;

    cart_item(String prodid, String item_name, String item_price, String total_price,String qauntity) {
        this.prodid = prodid;
        this.item_name = item_name;
        this.item_price = item_price;
        this.total_price = total_price;
        this.qauntity = qauntity;
     }

     public String getqauntity(){
        return qauntity;
     }

    public void setProdid(String prodid) {
        this.prodid = prodid;
    }

    public String getProdid() {
        return prodid;
    }
     public void setquantity(String qauntity){
        this.qauntity = qauntity;
     }

     public String getitem_name(){
        return item_name;
     }
     public void setitem_name(String item_name){
        this.item_name = item_name;
     }
     public String getitem_price(){
        return item_price;
     }
     public void setitem_price(String item_price){
        this.item_price =item_price;
     }
     public String gettotal_price(){
        return total_price;
     }
     public void settotal_price(String total_price){
        this.total_price = total_price;
     }
}
