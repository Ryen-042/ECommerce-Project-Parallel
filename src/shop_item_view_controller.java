import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.List;
import javax.swing.event.ChangeListener;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class shop_item_view_controller implements Initializable {

    private shop_item shop_item;
    @FXML
    private Button buy_btn;

    @FXML
    private Label item_name_lb;

    @FXML
    private Label item_price_lb;

    @FXML
    private Label item_total_price_lb;

    @FXML
    private Button put_in_cart_btn;

    @FXML
    private ComboBox<Integer> qauntity_cb ;

    Integer[] quantity = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};

    @FXML
    void buy_btn_clicked(ActionEvent event) throws IOException{
        int quantity = (int)qauntity_cb.getValue();
        if(!(quantity > 0))
            wrong("Enter valid quantity");
        else{
            try{    
                DataOutputStream dout=new DataOutputStream(App.getmysocket().getOutputStream());
                DataInputStream din=new DataInputStream(App.getmysocket().getInputStream());  
                dout.writeUTF("buy shop");
                dout.writeUTF(String.valueOf(quantity));
                dout.writeUTF(this.shop_item.getId());
                dout.flush();

                String str=(String)din.readUTF();  
                System.out.println(str);
                if (str.equals("ok"))
                    Done("Transaction is done");
                else if (str.equals("qnt not valid")){
                    wrong("No eough quantity in the shop");
                }    
                else if (str.equals("no")){
                    wrong("No such product");
                }
                else if(str.equals("You have no enough balance"))
                    wrong("You don't have enough balance");
                else{
                    wrong("Wrong connection");
                }

            }catch(Exception e){System.out.println(e);}  
        }
    }

    @FXML
    void put_in_cart_btn_clicked(ActionEvent event) {
        int quantity = (int)qauntity_cb.getValue();
        if(!(quantity > 0))
            wrong("Enter valid quantity");
        else{
            try{    
                DataOutputStream dout=new DataOutputStream(App.getmysocket().getOutputStream());
                DataInputStream din=new DataInputStream(App.getmysocket().getInputStream());  
                dout.writeUTF("put in cart");
                dout.writeUTF(String.valueOf(quantity));
                dout.writeUTF(this.shop_item.getId());
                dout.flush();

                String str=(String)din.readUTF();  
                System.out.println(str);
                if (str.equals("ok"))
                    Done("The item added to your cart successfully");
                else if (str.equals("qnt not valid"))
                    wrong("No eough quantity in the shop");
                else if (str.equals("qnt not valid2")){
                    int qnt=Integer.parseInt((String)din.readUTF());  
                    int orderqnt=Integer.parseInt((String)din.readUTF());  
                    wrong("The shop have only '"+qnt+"' items from this product (you already have '"+orderqnt+"' in cart)");
                }
                else if (str.equals("no")){
                    wrong("No such product");
                }
                else{
                    wrong("Wrong connection");
                }

            }catch(Exception e){System.out.println(e);}  
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub
        this.shop_item = shop_controller.getShop_item();
        item_name_lb.setText(shop_item.getitem_name());
        item_price_lb.setText(shop_item.getitem_price());

        qauntity_cb.getItems().removeAll(qauntity_cb.getItems());
        qauntity_cb.getItems().setAll(quantity);
        qauntity_cb.setValue(1);


        
    }

    @FXML
    void qauntity_changed(ActionEvent event) {
        item_total_price_lb.setText(Double.toString(Double.parseDouble(item_price_lb.getText())*qauntity_cb.getValue()));

    }

    void wrong(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error Dialog");
			alert.setHeaderText(null);
			alert.setContentText(message);
			alert.showAndWait();  
    }
    void Done(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Info Dialog");
			alert.setHeaderText(null);
			alert.setContentText(message);
			alert.showAndWait();  
    }
    
}

    

