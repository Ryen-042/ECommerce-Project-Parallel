import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

public class cart_item_view_controller implements Initializable{

    private cart_item cart_item;
    @FXML
    private Button buy_btn;

    @FXML
    private Button delete_btn;

    @FXML
    private Button edit_btn;

    @FXML
    private Label item_name_lb;

    @FXML
    private Label item_price_lb;

    @FXML
    private Label item_total_price_lb;

    @FXML
    private ComboBox<Integer> qauntity_cb;

    Integer[] quantity = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};


    @FXML
    void buy_btn_clicked(ActionEvent event) {
                int quantity = (int)qauntity_cb.getValue();
        if(!(quantity > 0))
            wrong("Enter valid quantity");
        else{
            try{    
                DataOutputStream dout=new DataOutputStream(App.getmysocket().getOutputStream());
                DataInputStream din=new DataInputStream(App.getmysocket().getInputStream());  
                dout.writeUTF("buy cart");
                dout.writeUTF(String.valueOf(quantity));
                dout.writeUTF(this.cart_item.getProdid());
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
    void delete_btn_clicked(ActionEvent event) {
        try{    
            DataOutputStream dout=new DataOutputStream(App.getmysocket().getOutputStream());
            DataInputStream din=new DataInputStream(App.getmysocket().getInputStream());  
            dout.writeUTF("delete cart");
            dout.writeUTF(this.cart_item.getProdid());
            dout.flush();

            String str=(String)din.readUTF();  
            System.out.println(str);
            if (str.equals("ok"))
                Done("The item is deleted");
            else if (str.equals("no")){
                wrong("No such product");
            }
            else{
                wrong("Wrong connection");
            }
        }catch(Exception e){System.out.println(e);}  

    }

    @FXML
    void edit_btn_clicked(ActionEvent event) {
        int quantity = (int)qauntity_cb.getValue();
        if(!(quantity > 0))
            wrong("Enter valid quantity");
        else{
            try{    
                DataOutputStream dout=new DataOutputStream(App.getmysocket().getOutputStream());
                DataInputStream din=new DataInputStream(App.getmysocket().getInputStream());  
                dout.writeUTF("edit cart");
                dout.writeUTF(String.valueOf(quantity));
                dout.writeUTF(this.cart_item.getProdid());
                dout.flush();

                String str=(String)din.readUTF();  
                System.out.println(str);
                if (str.equals("cart ok")){
                    Done("The quantity is edited succefully");
                }
                else if (str.equals("qnt not valid"))
                    wrong("No eough quantity in the shop");
                else{
                    wrong("Wrong connection");
                }                
            }catch(Exception e){System.out.println(e);}  
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.cart_item = cart_scene_controller.getcart_item();
        item_name_lb.setText(cart_item.getitem_name());
        item_price_lb.setText(cart_item.getitem_price());

        qauntity_cb.getItems().removeAll(qauntity_cb.getItems());
        qauntity_cb.getItems().setAll(quantity);
        qauntity_cb.setValue(Integer.parseInt(cart_item.getqauntity()));
        item_total_price_lb.setText(Double.toString(Double.parseDouble(item_price_lb.getText())*qauntity_cb.getValue()));
        


        
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
