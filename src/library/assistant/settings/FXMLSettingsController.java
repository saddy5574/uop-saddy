package library.assistant.settings;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import library.assistant.allerts.MakeAlert;

public class FXMLSettingsController implements Initializable {

    @FXML
    private JFXTextField nDaysWithoutFine;
    @FXML
    private JFXTextField nFinePerDay;
    @FXML
    private JFXTextField userName;
    @FXML
    private JFXPasswordField password;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            initDefaultValues();
        } catch (IOException ex) {
            Logger.getLogger(FXMLSettingsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @FXML
    private void handleSaveButtonAction(ActionEvent event) {
        try {
            Preferences preferences = Preferences.getPreferences();
            preferences.setnDaysWithoutFine(Integer.parseInt(nDaysWithoutFine.getText()));
            preferences.setFinePerDay(Float.parseFloat(nFinePerDay.getText()));
            preferences.setUserName(userName.getText());
            preferences.setPassword(password.getText());
            Preferences.writePrefrencesToFile(preferences);
            
            MakeAlert.showInforamtionAlert("Success","Settings is Updated");
            } catch (IOException ex) {
            Logger.getLogger(FXMLSettingsController.class.getName()).log(Level.SEVERE, null, ex);
            MakeAlert.showWrrorAlert("Failed","Setting can not save to Configuration");
        }        
    }

    @FXML
    private void handleCAncelButtonAction(ActionEvent event) {
        Stage stage = (Stage) nDaysWithoutFine.getScene().getWindow();
        stage.close();
    }

    private void initDefaultValues() throws IOException {
        nDaysWithoutFine.setText(String.valueOf(Preferences.getPreferences().getnDaysWithoutFine()));
        nFinePerDay.setText(String.valueOf(Preferences.getPreferences().getFinePerDay()));
        userName.setText(String.valueOf(Preferences.getPreferences().getUserName()));
        password.setText(String.valueOf(Preferences.getPreferences().getPassword()));
    }    
}
