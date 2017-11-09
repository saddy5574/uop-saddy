package library.assistant.ui.main;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import library.assistant.database.DatabaseHandler;
import org.apache.derby.database.Database;

public class main extends Application {
    
    @Override
    public void start(Stage primaryStage) throws IOException {
       
        Parent root = FXMLLoader.load(getClass().getResource("/library/assistant/login/FXMLLogin.fxml"));
        
        Scene scene = new Scene(root);
        
        primaryStage.setScene(scene);
        primaryStage.show();
        
        new Thread(() -> {
            DatabaseHandler.getIntance();
        }).start();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
