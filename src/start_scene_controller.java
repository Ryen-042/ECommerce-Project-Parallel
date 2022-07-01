import java.io.IOException;
import java.net.Socket;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
public class start_scene_controller {

    @FXML
    private Button login_btn;

    @FXML
    private Button register_btn;

    @FXML
    void log_btn_clicked(ActionEvent event) throws IOException {
        Parent log_in_parent = FXMLLoader.load(getClass().getResource( "login_scene.fxml"));
        Scene log_in = new Scene(log_in_parent);
        Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
        window.setScene(log_in);
        window.show();


    }

    @FXML
    void reg_btn_clicked(ActionEvent event) throws IOException {
        Parent reg_parent = FXMLLoader.load(getClass().getResource( "register_scene.fxml"));
        Scene reg = new Scene(reg_parent);
        Stage window = (Stage)((Node) event.getSource()).getScene().getWindow();
        window.setScene(reg);
        window.show();
    }
}
