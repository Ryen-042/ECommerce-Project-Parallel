import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.control.Alert;

public class register_scene_controller {
    @FXML
    private Button back_btn;

    @FXML
    private TextField card_tx;

    @FXML
    private TextField email_tx;

    @FXML
    private TextField name_tx;

    @FXML
    private PasswordField pass_tx;

    @FXML
    private Button register_btn;

    @FXML
    private TextField user_tx;

    @FXML
    void go_back_btn_clicked(ActionEvent event) throws IOException {
        Parent start_parent = FXMLLoader.load(getClass().getResource( "start.fxml"));
        Scene start = new Scene(start_parent);
        Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
        window.setScene(start);
        window.show();
    }

    @FXML
    void reg_btn_clicked(ActionEvent event) throws IOException {
        String name = name_tx.getText();
        String username = user_tx.getText();
        String password = pass_tx.getText();
        String email = email_tx.getText();

        if(name.isEmpty() || username.isEmpty() || password.isEmpty() || email.isEmpty())
            wrong_reg("Please Enter all data!");
        else{
            try{
                App.setmysocket(new Socket("localhost",1978)); //"192.168.1.7"
                DataOutputStream dout=new DataOutputStream(App.getmysocket().getOutputStream());
                DataInputStream din=new DataInputStream(App.getmysocket().getInputStream());  
                dout.writeUTF("register");
                dout.writeUTF(name);
                dout.writeUTF(username);
                dout.writeUTF(password);
                dout.writeUTF(email);
                dout.flush(); 
                String str=(String)din.readUTF();  
                System.out.println(str);
                if (str.equals("ok")){
                    reg(event);
                }    
                else if (str.equals("dublicate username")){
                    wrong_reg("dublicate username");
                }
                else{
                    wrong_connection();
                }

            }catch(Exception e){System.out.println(e);}          
        }
    } 
    
    void reg(ActionEvent event) throws IOException {

        
        Parent home_parent = FXMLLoader.load(getClass().getResource( "home.fxml"));
        Scene home = new Scene(home_parent);
        Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
        window.setScene(home);
        window.show();
    }
    void wrong_reg(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Error Dialog");
			alert.setHeaderText(null);
			alert.setContentText(message);
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
