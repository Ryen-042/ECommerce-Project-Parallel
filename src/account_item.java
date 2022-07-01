import java.io.Serializable;

public class account_item implements Serializable{
    String item_name;
    String quantity;
    String date;

    account_item(String item_name, String quantity, String date) {
        this.item_name = new String(item_name);
        this.quantity = quantity;
     }

    public String getDate() {
        return date;
    }

     public String getitem_name(){
        return item_name;
     }
     public void setitem_name(String item_name){
        this.item_name = item_name;
     }
     public String getquantity(){
        return quantity;
     }
     public void setquantity(String quantity){
        this.quantity =quantity;
     }
}
