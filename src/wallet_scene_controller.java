import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.control.Alert;
public class wallet_scene_controller {
    @FXML
    private Button back_btn;

    @FXML
    private Button deposit_btn;

    @FXML
    private TextField amount_tx;

    @FXML
    void back_btn_clicked(ActionEvent event) throws IOException {
        Parent start_parent = FXMLLoader.load(getClass().getResource( "account_scene.fxml"));
        Scene start = new Scene(start_parent);
        Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
        window.setScene(start);
        window.show();
    }

    @FXML
    void deposit_btn_clicked(ActionEvent event) throws IOException {
        String amounttx = amount_tx.getText();
        int amount = Integer.parseInt(amounttx);
        if (amount <= 0)
            wrong_amount();
            try{    
                App.setmysocket(new Socket("localhost",1978)); //"192.168.1.7"
                DataOutputStream dout=new DataOutputStream(App.getmysocket().getOutputStream());
                DataInputStream din=new DataInputStream(App.getmysocket().getInputStream());  
                dout.writeUTF("deposite");
                dout.writeUTF(amounttx);
                dout.flush(); 
                String str=(String)din.readUTF();  
                System.out.println(str);
                if (str.equals("ok")){
                    // Done message                
                    Parent start_parent = FXMLLoader.load(getClass().getResource( "account_scene.fxml"));
                    Scene start = new Scene(start_parent);
                    Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
                    window.setScene(start);
                    window.show();
                }   
                else{
                    wrong_connection();
                }
                
        }catch(Exception e){System.out.println(e);}  

    }    
    void wrong_amount(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Enter valid amount!");
        alert.showAndWait();  
    }
    void wrong_connection(){
    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Dialog");
                    alert.setHeaderText(null);
                    alert.setContentText("cant connect to server");
                    alert.showAndWait();  
    }

}
