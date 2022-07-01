import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

public class admin_scene_controller implements Initializable {

    @FXML
    private TableView<admin_table_item> admin_table;

    @FXML
    private TableColumn<admin_table_item, String> client_c;

    @FXML
    private Button home_btn;

    @FXML
    private TableColumn<admin_table_item, String> item_bought_c;

    @FXML
    private TableColumn<admin_table_item, String> item_date_bought_c;

    @FXML
    private TableColumn<admin_table_item, String> item_price_c;

    @FXML
    private TableColumn<admin_table_item, String> item_quantity_c;

    @FXML
    private TableColumn<admin_table_item, String> item_total_price_c;

    @FXML
    private Label profit_lb;

    @FXML
    private Button refresh_btn;

    private String profit;

    private ObservableList<admin_table_item> data;

    @FXML
    void home_btn_clicked(ActionEvent event) throws IOException {
        // copy the log out of home page function here

        DataInputStream din=new DataInputStream(App.getmysocket().getInputStream());  
        DataOutputStream dout=new DataOutputStream(App.getmysocket().getOutputStream());
        dout.writeUTF("QUIT");
        dout.flush();
        dout.close();
        App.getmysocket().close();
        Parent start_parent = FXMLLoader.load(getClass().getResource( "start.fxml"));
        Scene start = new Scene(start_parent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(start);
        window.show();
    }

    @FXML
    void refresh_btn_clicked(ActionEvent event) {

        try{                  
            DataOutputStream dout=new DataOutputStream(App.getmysocket().getOutputStream());
            DataInputStream din=new DataInputStream(App.getmysocket().getInputStream());  
            dout.writeUTF("admin");
            
            int num=Integer.parseInt((String)din.readUTF());  
            int sum=Integer.parseInt((String)din.readUTF());  
            profit_lb.setText(String.valueOf(sum));
                        
            String username="",prodname="", qnt="", price="",date="", total="";
            data = FXCollections.observableArrayList();
            admin_table_item item;
            if (num > 0){
                for (int i = 0; i < num; i++)
                {
                    username = (String)din.readUTF();
                    prodname = (String)din.readUTF();
                    qnt = (String)din.readUTF();
                    price = (String)din.readUTF();
                    total = Float.toString(Float.parseFloat(price)*Integer.parseInt(qnt));
                    date = (String)din.readUTF();
                    item = new admin_table_item(username,prodname, price,qnt,total,date);
                    data.add(item);
                }
                admin_table.setItems(data);                
            }
            
        }catch(Exception e){System.out.println(e);}  

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub

        client_c.setCellValueFactory(f -> new SimpleStringProperty(f.getValue().getclient()));
        item_price_c.setCellValueFactory(f -> new SimpleStringProperty(f.getValue().getitem_price()));
        item_bought_c.setCellValueFactory(f -> new SimpleStringProperty(f.getValue().getitem_name()));
        item_quantity_c.setCellValueFactory(f -> new SimpleStringProperty(f.getValue().getitem_quantity()));
        item_total_price_c.setCellValueFactory(f -> new SimpleStringProperty(f.getValue().getitem_total_price()));    
        item_date_bought_c.setCellValueFactory(f -> new SimpleStringProperty(f.getValue().getdate()));
        

        try{                  
            DataOutputStream dout=new DataOutputStream(App.getmysocket().getOutputStream());
            DataInputStream din=new DataInputStream(App.getmysocket().getInputStream());  
            dout.writeUTF("admin");
            
            int num=Integer.parseInt((String)din.readUTF());  
            int sum=Integer.parseInt((String)din.readUTF());  
            profit_lb.setText(String.valueOf(sum));
                        
            String username="",prodname="", qnt="", price="",date="", total="";
            data = FXCollections.observableArrayList();
            admin_table_item item;
            if (num > 0){
                for (int i = 0; i < num; i++)
                {
                    username = (String)din.readUTF();
                    prodname = (String)din.readUTF();
                    qnt = (String)din.readUTF();
                    price = (String)din.readUTF();
                    total = Float.toString(Float.parseFloat(price)*Integer.parseInt(qnt));
                    date = (String)din.readUTF();
                    item = new admin_table_item(username,prodname, price,qnt,total,date);
                    data.add(item);
                }
                admin_table.setItems(data);                
            }
            
        }catch(Exception e){System.out.println(e);}  


    }

}