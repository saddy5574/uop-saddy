package library.assistant.login;

import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import library.assistant.settings.Preferences;
import library.assistant.ui.main.FXMLMainController;
import org.apache.commons.codec.digest.DigestUtils;


public class FXMLLoginController implements Initializable {

    @FXML
    private JFXTextField userName;
    @FXML
    private JFXTextField password;

    Preferences preferences;    
    @FXML
    private Label labelLogin;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            preferences = Preferences.getPreferences();
        } catch (IOException ex) {
            Logger.getLogger(FXMLLoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @FXML
    private void saveActionperform(ActionEvent event) {  
        labelLogin.setText("Library Assistant Login");
        labelLogin.setStyle("-fx-background-color:black");

        String userN = userName.getText();
        String passW = DigestUtils.shaHex(password.getText());
        
        if (userN.equals(preferences.getUserName())&&passW.equals(preferences.getPassword())) {
            closeStage();
            loadWindow();
        } else {
            labelLogin.setText("Invalid Creditional");
            labelLogin.setStyle("-fx-background-color:#F44336");
        }   
    }

    @FXML
    private void cancel(ActionEvent event) {
        System.exit(0);
    }
    
    void loadWindow() {
        Parent parent;
        try {
            parent = FXMLLoader.load(getClass().getResource("/library/assistant/ui/main/FXMLMain.fxml"));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Library Assistant");
            stage.setScene(new Scene(parent));
            stage.show();

        } catch (IOException ex) {
            Logger.getLogger(FXMLMainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void closeStage() {
        ((Stage) labelLogin.getScene().getWindow()).close();
    }

}
