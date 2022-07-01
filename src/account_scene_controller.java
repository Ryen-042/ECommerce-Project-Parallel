import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
public class account_scene_controller implements Initializable {

    @FXML
    private TableView<account_item> history_table;

    @FXML
    private TableColumn<account_item, String> item_name;

    @FXML
    private TableColumn<account_item, String> quantity;

//    @FXML
//    private TableColumn<account_item, String> date;

    @FXML
    private TextField balance_tx;

    @FXML
    private TextField email_tx;

    @FXML
    private Button home_btn;

    @FXML
    private TextField name_tx;

    @FXML
    private TextField user_tx;

    @FXML
    private Button wallet_btn;
    private ObservableList<account_item> data;

    @FXML
    void home_btn_clicked(ActionEvent event) throws IOException {
        Parent start_parent = FXMLLoader.load(getClass().getResource( "home.fxml"));
        Scene start = new Scene(start_parent);
        Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
        window.setScene(start);
        window.show();
    }

    @FXML
    void wallet_btn_clicked(ActionEvent event) throws IOException {
        Parent start_parent = FXMLLoader.load(getClass().getResource( "wallet_scene.fxml"));
        Scene start = new Scene(start_parent);
        Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
        window.setScene(start);
        window.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub

        // here goes function for viewing the history 
        // and account info 
        try{                  
            DataOutputStream dout=new DataOutputStream(App.getmysocket().getOutputStream());
            DataInputStream din=new DataInputStream(App.getmysocket().getInputStream());  
            dout.writeUTF("Get account info");
            String username=(String)din.readUTF();  
            String name=(String)din.readUTF();  
            String email=(String)din.readUTF();  
            String balance=(String)din.readUTF();
            
            user_tx.setText(username);
            name_tx.setText(name);
            email_tx.setText(email);
            balance_tx.setText(balance);

            dout.writeUTF("Get purchase info");
            String pname="", qnt="", date="";
            data = FXCollections.observableArrayList();
            account_item item;
            String str = (String)din.readUTF();    
            if (!"no".equals(str)){
                int num = Integer.parseInt(str);
                for (int i = 0; i < num; i++)
                {
                    pname = (String)din.readUTF();
                    qnt = (String)din.readUTF();
                    date = (String)din.readUTF();
                    item = new account_item(pname, qnt,date);
                    data.add(item);
                }
                history_table.setItems(data);                
            }
            
        }catch(Exception e){System.out.println(e);}  
        item_name.setCellValueFactory(f -> new SimpleStringProperty(f.getValue().getitem_name()));
        quantity.setCellValueFactory(f -> new SimpleStringProperty(f.getValue().getquantity()));
//        date.setCellValueFactory(f -> new SimpleStringProperty(f.getValue().getDate()));


    }
}
