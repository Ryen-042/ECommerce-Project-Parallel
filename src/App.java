import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class App extends Application {
    
    private static Socket mysocket;
    
    public static Socket getmysocket() {
        return mysocket;
    }

    public static void setmysocket(Socket mysocket) {
        App.mysocket = mysocket;
    }
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("start.fxml"));
        
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args)  {
        launch(args);

    }
}
