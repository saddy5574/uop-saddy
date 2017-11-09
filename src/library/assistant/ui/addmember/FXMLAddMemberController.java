package library.assistant.ui.addmember;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import library.assistant.database.DatabaseHandler;

public class FXMLAddMemberController implements Initializable {

    @FXML
    private JFXTextField name;
    @FXML
    private JFXTextField id;
    @FXML
    private JFXTextField mobile;
    @FXML
    private JFXTextField email;
    @FXML
    private JFXButton bAddMember;
    @FXML
    private JFXButton bCancel;
    @FXML
    private AnchorPane rootPane;
    
    
    DatabaseHandler databaseHandler;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        databaseHandler = DatabaseHandler.getIntance();
    }    


    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void addMember(ActionEvent event) {
         String id = this.id.getText().toLowerCase();
        String name = this.name.getText();
        String mobile = this.mobile.getText();
        String email = this.email.getText();
        
        Boolean flag = id.isEmpty() || name.isEmpty() || mobile.isEmpty() || email.isEmpty();
        
        if (flag) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please Enter data in all field!");
            alert.showAndWait();
            return;
        }
        
        String qu = "INSERT INTO MEMBER VALUES ("+
                "'" + id + "',"+
                "'" + name + "',"+
                "'" + mobile + "',"+
                "'" + email + "'"+
                ")";
        System.out.println(qu);
        
        if (databaseHandler.execAction(qu)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Success.");
            alert.showAndWait();
            clearAddMemberForm();
            return;
        } else { //Error
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Failed");
            alert.showAndWait();
            return;
        }
    
    }

    private void clearAddMemberForm() {
        id.setText("");
        name.setText("");
        mobile.setText("");
        email.setText("");
    }
    
}
