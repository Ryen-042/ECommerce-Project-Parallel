import java.io.Serializable;

public class admin_table_item  implements Serializable{
    String client;
    String item_name;
    String item_price;
    String item_quantity;
    String item_total_price;
    String date;

    admin_table_item(String client,String item_name,String item_price,String item_quantity,String item_total_price,String date) {
        this.client = client;
        this.item_name = item_name;
        this.item_price = item_price;
        this.item_quantity = item_quantity;
        this.item_total_price = item_total_price;
        this.date = date ; 
     }

     public String getclient(){
        return client;
     }
     public String getitem_name(){
        return item_name;
     }
     public String getitem_price(){
        return item_price;
     }
     public String getitem_quantity(){
        return item_quantity;
     }
     public String getitem_total_price(){
        return item_total_price;
     }
     public String getdate(){
        return date;
     }
     public void setclient(String client){
        this.client = client;
     }
     public void setitem_name(String item_name){
        this.item_name = item_name;
     }
     public void setitem_price(String item_price){
        this.item_price = item_price;
     }
     public void setitem_quantity(String item_quantity){
        this.item_quantity = item_quantity;
     }
     public void setitem_total_price(String item_total_price){
        this.item_total_price = item_total_price;
     }
     public void setdate(String date){
        this.date = date;
     }
     
}
