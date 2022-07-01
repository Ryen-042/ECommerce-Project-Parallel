import java.io.Serializable;

public class shop_item implements Serializable{

    String id;
    String item_name;
    String item_price;
    String category;

    shop_item(String id, String item_name, String item_price, String category) {
        this.id = id;
        this.item_name = new String(item_name);
        this.item_price = item_price;
        this.category = new String(category);
     }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
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
     public String getcategory(){
        return category;
     }
     public void setcategory(String category){
        this.category = category;
     }
}
