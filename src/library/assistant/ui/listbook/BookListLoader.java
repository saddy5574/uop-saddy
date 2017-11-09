package library.assistant.ui.listbook;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author SAYYAD
 */
public class BookListLoader extends Application {
    
    @Override
    public void start(Stage primaryStage) throws IOException {
       
        Parent root = FXMLLoader.load(getClass().getResource("FXMLListBook.fxml"));
        
        Scene scene = new Scene(root);
        
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
