import java.io.DataInputStream;
import java.io.DataOutputStream;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ListChangeListener.Change;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
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
import javafx.scene.control.Alert;
public class shop_controller implements Initializable {
    
    private static shop_item shop_item;

    public static shop_item getShop_item() {
        return shop_item;
    }

    public static void setmysocket(shop_item myShop_item) {
        shop_controller.shop_item = myShop_item;
    }


    @FXML
    private ComboBox<String> category_cb;

    @FXML
    private Button home_btn;

    @FXML
    private Button search_btn;

    @FXML
    private TableView<shop_item> shop_table;

    @FXML
    private TableColumn<shop_item,String > item_name_c;

    @FXML
    private TableColumn<shop_item,String> item_price_c;

    @FXML
    private TextField search_tx;

    private ObservableList<shop_item> data;
    
    String  [] category = {"sports", "food", "others"};

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try{
            String name="", price="", category="", id="";
            shop_item s;
            data = FXCollections.observableArrayList();
            DataOutputStream dout=new DataOutputStream(App.getmysocket().getOutputStream());
            DataInputStream din=new DataInputStream(App.getmysocket().getInputStream());  
            dout.writeUTF("Get shop data");
            dout.flush();

            String str = (String)din.readUTF();    
            if (!"no".equals(str)){
                int num = Integer.parseInt(str);
                for (int i = 0; i < num; i++)
                {
                    id = (String)din.readUTF();
                    name = (String)din.readUTF();
                    price = (String)din.readUTF();
                    category = (String)din.readUTF();
                    s = new shop_item(id, name, price, category);
                    data.add(s);
                }
                shop_table.setItems(data);                
            }

        }catch(Exception e){System.out.println(e);}  

        shop_table.setOnMouseClicked((MouseEvent event) -> {
            if(event.getButton().equals(MouseButton.PRIMARY)){
                shop_item = shop_table.getSelectionModel().getSelectedItem();
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("shop_item_view.fxml"));
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

        category_cb.getItems().removeAll(category_cb.getItems());
        category_cb.getItems().setAll(category);

        

    }
    @FXML
    void home_btn_clicked(ActionEvent event) throws IOException {
        Parent start_parent = FXMLLoader.load(getClass().getResource( "home.fxml"));
        Scene start = new Scene(start_parent);
        Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
        window.setScene(start);
        window.show();
    }

    @FXML
    void search_btn_clicked(ActionEvent event) {
        try{
            String name="", price="", category="", id="";
            shop_item s;
            String word = search_tx.getText();
            String categorys = category_cb.getValue();
            data = FXCollections.observableArrayList();
            DataOutputStream dout=new DataOutputStream(App.getmysocket().getOutputStream());
            DataInputStream din=new DataInputStream(App.getmysocket().getInputStream());  
            if (word.isEmpty())
            {
                dout.writeUTF("Get shop data");
            }
            else if (categorys == null && !word.isEmpty())
            {
                dout.writeUTF("word search");
                dout.writeUTF(word);
            }
            else{
                dout.writeUTF("search");
                dout.writeUTF(word);
                dout.writeUTF(categorys);
            }
            dout.flush();

            String str = (String)din.readUTF();    
            if (!"no".equals(str)){
                int num = Integer.parseInt(str);
                for (int i = 0; i < num; i++)
                {
                    id = (String)din.readUTF();
                    name = (String)din.readUTF();
                    price = (String)din.readUTF();
                    category = (String)din.readUTF();
                    s = new shop_item(id, name, price, category);
                    data.add(s);
                }
                shop_table.setItems(data);                
            }else{
                shop_table.getItems().clear();
            }

        }catch(Exception e){System.out.println(e);}  

//        shop_table.setOnMouseClicked((MouseEvent event) -> {
//            if(event.getButton().equals(MouseButton.PRIMARY)){
//                shop_item = shop_table.getSelectionModel().getSelectedItem();
//                try {
//                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("shop_item_view.fxml"));
//                    Parent root1 = (Parent) fxmlLoader.load();
//                    Stage stage = new Stage();
//                    stage.setScene(new Scene(root1));
//                    stage.showAndWait();
//                
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });

        
    }
    void wrong(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error Dialog");
			alert.setHeaderText(null);
			alert.setContentText(message);
			alert.showAndWait();  
    }
    
}
