
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Node;

public class home_controller {
    @FXML
    private Button account_btn;

    @FXML
    private Button cart_btn;

    @FXML
    private Button logout_btn;

    @FXML
    private Button shop_btn;

    @FXML
    void account_btn_clicked(ActionEvent event) throws IOException {
        Parent start_parent = FXMLLoader.load(getClass().getResource( "account_scene.fxml"));
        Scene start = new Scene(start_parent);
        Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
        window.setScene(start);
        window.show();
    }

    @FXML
    void cart_btn_clicked(ActionEvent event) throws IOException {
        Parent start_parent = FXMLLoader.load(getClass().getResource( "cart_scene.fxml"));
        Scene start = new Scene(start_parent);
        Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
        window.setScene(start);
        window.show();
    }

    @FXML
    void logout_btn_clicked(ActionEvent event) throws IOException {

        
        DataInputStream din=new DataInputStream(App.getmysocket().getInputStream());  
        DataOutputStream dout=new DataOutputStream(App.getmysocket().getOutputStream());
        dout.writeUTF("QUIT");
        dout.flush();
        dout.close();
        App.getmysocket().close();
        Parent start_parent = FXMLLoader.load(getClass().getResource( "start.fxml"));
        Scene start = new Scene(start_parent);
        Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
        window.setScene(start);
        window.show();
    }

    @FXML
    void shop_btn_clicked(ActionEvent event) throws IOException {

        Parent start_parent = FXMLLoader.load(getClass().getResource( "shop_scene.fxml"));
        Scene start = new Scene(start_parent);
        Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
        window.setScene(start);
        window.show();


         
    }    
}
