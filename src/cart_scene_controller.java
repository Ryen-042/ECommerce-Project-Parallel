import java.io.DataInputStream;
import java.io.DataOutputStream;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
public class cart_scene_controller implements Initializable {


    @FXML
    private TableView<cart_item> cart_table;

    @FXML
    private TableColumn<cart_item, String> item_name_c;

    @FXML
    private TableColumn<cart_item, String> item_price_c;

    @FXML
    private TableColumn<cart_item, String> item_qauntity_c;

    @FXML
    private TableColumn<cart_item, String> item_total_price_c;
    
    @FXML
    private Button home_btn;

    @FXML
    private Button refresh_btn;

    private static cart_item cart_item;

    private ObservableList<cart_item> data;


    public static cart_item getcart_item() {
        return cart_item;
    }

    public static void setmysocket(cart_item mycart_item) {
        cart_scene_controller.cart_item = mycart_item;
    }


    @FXML
    void home_btn_clicked(ActionEvent event) throws IOException {
        Parent start_parent = FXMLLoader.load(getClass().getResource( "home.fxml"));
        Scene start = new Scene(start_parent);
        Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
        window.setScene(start);
        window.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try{
            String name="", price="", qnt="", total="", prodid = "";
            cart_item item;
            data = FXCollections.observableArrayList();
            DataOutputStream dout=new DataOutputStream(App.getmysocket().getOutputStream());
            DataInputStream din=new DataInputStream(App.getmysocket().getInputStream());  
            dout.writeUTF("Get cart data");
            String str = (String)din.readUTF();    
            if (!"no".equals(str)){
                int num = Integer.parseInt(str);
                for (int i = 0; i < num; i++)
                {
                    prodid = (String)din.readUTF();
                    name = (String)din.readUTF();
                    price = (String)din.readUTF();
                    qnt = (String)din.readUTF();
                    total = Float.toString(Integer.parseInt(qnt)*Float.parseFloat(price));
                    item = new cart_item(prodid, name, price, total, qnt);
                    data.add(item);
                }
                cart_table.setItems(data);                
            }else{
                cart_table.getItems().clear();
            }

        }catch(Exception e){System.out.println(e);}  
        
        cart_table.setOnMouseClicked((MouseEvent event) -> {
            if(event.getButton().equals(MouseButton.PRIMARY)){
                cart_item = cart_table.getSelectionModel().getSelectedItem();
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("cart_item_view.fxml"));
                    Parent root1 = (Parent) fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root1));
                    stage.showAndWait();
                
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        item_name_c.setCellValueFactory(f -> new SimpleStringProperty(f.getValue().getitem_name()));
        item_price_c.setCellValueFactory(f -> new SimpleStringProperty(f.getValue().getitem_price()));
        item_total_price_c.setCellValueFactory(f -> new SimpleStringProperty(f.getValue().gettotal_price()));    
        item_qauntity_c.setCellValueFactory(f -> new SimpleStringProperty(f.getValue().getqauntity()));

    }    

    @FXML
    void refresh_btn_clicked(ActionEvent event) {
        try{
            String name="", price="", qnt="", total="", prodid = "";
            cart_item item;
            data = FXCollections.observableArrayList();
            DataOutputStream dout=new DataOutputStream(App.getmysocket().getOutputStream());
            DataInputStream din=new DataInputStream(App.getmysocket().getInputStream());  
            dout.writeUTF("Get cart data");
            String str = (String)din.readUTF();    
            if (!"no".equals(str)){
                int num = Integer.parseInt(str);
                for (int i = 0; i < num; i++)
                {
                    prodid = (String)din.readUTF();
                    name = (String)din.readUTF();
                    price = (String)din.readUTF();
                    qnt = (String)din.readUTF();
                    total = Float.toString(Integer.parseInt(qnt)*Float.parseFloat(price));
                    item = new cart_item(prodid, name, price, total, qnt);
                    data.add(item);
                }
                cart_table.setItems(data);                
            }else{
                cart_table.getItems().clear();
            }

        }catch(Exception e){System.out.println(e);}  

    }
    
}
