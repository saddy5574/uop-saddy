/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library.assistant.ui.addbook;

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

/**
 * FXML Controller class
 *
 * @author SAYYAD
 */
public class FXMLAddBookController implements Initializable {

    @FXML
    private JFXTextField tfTitle;
    @FXML
    private JFXTextField tfId;
    @FXML
    private JFXTextField tfAuthor;
    @FXML
    private JFXTextField tfPublisher;
    @FXML
    private JFXButton bSave;
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
    private void addBook(ActionEvent event) {
        String bookID = tfId.getText().toLowerCase();
        String bookName = tfTitle.getText();
        String bookAuthor = tfAuthor.getText();
        String bookPublisher = tfPublisher.getText();
        
        if (bookID.isEmpty() || bookName.isEmpty() || bookAuthor.isEmpty() || bookPublisher.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please Enter data in all field!");
            alert.showAndWait();
            return;
        }
        
        /*stmt.execute("CREATE TABLE " + TABLE_NAME + "("
                        + "     id varchar(200) primary key,\n"
                        + "     title varchar(200),\n"
                        + "     author varchar(200),\n"
                        + "     publisher varchar(100),\n"
                        + "     isAvail boolean default true"
                        + " )");*/
        
        String qu = "INSERT INTO BOOK VALUES ("+
                "'" + bookID + "',"+
                "'" + bookName + "',"+
                "'" + bookAuthor + "',"+
                "'" + bookPublisher + "',"+
                "'" + true + "'"+
                ")";
        System.out.println(qu);
        
        if (databaseHandler.execAction(qu)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Success.");
            alert.showAndWait();
            clearAddBookForm();
            return;
        } else { //Error
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Failed");
            alert.showAndWait();
            return;
        }
    
    }

    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }
    
    private void clearAddBookForm(){
        tfId.setText("");
        tfAuthor.setText("");
        tfTitle.setText("");
        tfPublisher.setText("");
    }
    
}
