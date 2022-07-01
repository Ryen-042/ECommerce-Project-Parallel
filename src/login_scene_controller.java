import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
public class login_scene_controller {
    @FXML
    private Button back_btn;

    @FXML
    private Button login_btn;

    @FXML
    private PasswordField pass_tx;

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
    void log_in_btn_clicked(ActionEvent event) throws IOException {
        String user = user_tx.getText();
        String pass = pass_tx.getText();
        try{    
              
            App.setmysocket(new Socket("localhost",1978)); //"192.168.1.7"
            DataOutputStream dout=new DataOutputStream(App.getmysocket().getOutputStream());
            DataInputStream din=new DataInputStream(App.getmysocket().getInputStream());  
            dout.writeUTF("check log in");
            dout.writeUTF(user);
            dout.writeUTF(pass);
            dout.flush(); 
            String str=(String)din.readUTF();  
            System.out.println(str);
            if (str.equals("okc")){

                log_in(event);
            }    
            else if (str.equals("noc")){
                wrong_log_in();

            }
            else if (str.equals("admin"))
            {
                Parent home_parent = FXMLLoader.load(getClass().getResource( "admin_scene.fxml"));
                Scene home = new Scene(home_parent);
                Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
                window.setScene(home);
                window.show();                
            }
            else{
                wrong_connection();
            }
                            
        }catch(Exception e){System.out.println(e);}  
        
              
        
    }

    void log_in(ActionEvent event) throws IOException {

        
        Parent home_parent = FXMLLoader.load(getClass().getResource( "home.fxml"));
        Scene home = new Scene(home_parent);
        Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
        window.setScene(home);
        window.show();
    }
    void wrong_log_in(){
        Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Dialog");
			alert.setHeaderText(null);
			alert.setContentText("wrong user name or password!");
			alert.showAndWait();  
    }
    void wrong_connection(){
        Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error Dialog");
			alert.setHeaderText(null);
			alert.setContentText("cant connect to server");
			alert.showAndWait();  
    }

}
