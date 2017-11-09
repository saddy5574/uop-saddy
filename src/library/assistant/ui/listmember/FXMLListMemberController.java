
package library.assistant.ui.listmember;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import library.assistant.database.DatabaseHandler;
import library.assistant.ui.addbook.FXMLAddBookController;

public class FXMLListMemberController implements Initializable {
    
    ObservableList<Member> list  = FXCollections.observableArrayList();
    
    @FXML
    private AnchorPane rootPane;
    @FXML
    private TableView<Member> memberView;
    @FXML
    private TableColumn<Member, String> idCol;
    @FXML
    private TableColumn<Member, String> nameCol;
    @FXML
    private TableColumn<Member, String> mobileCol;
    @FXML
    private TableColumn<Member, String> emailCol;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
       initCol();
       loadData();
    }    
    
      private void initCol() {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        mobileCol.setCellValueFactory(new PropertyValueFactory<>("mobile"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));        
    }

    private void loadData() {
        DatabaseHandler databaseHandler = DatabaseHandler.getIntance();
        String qu = "SELECT * FROM MEMBER";
        ResultSet resultSet = databaseHandler.execQuery(qu);
        
        try {
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String name = resultSet.getString("name");
                String mobie = resultSet.getString("mobile");
                String email = resultSet.getString("email");

                
                list.add(new Member(id, name, mobie, email));
            }
        } catch (SQLException e) {
            Logger.getLogger(FXMLAddBookController.class.getName()).log(Level.SEVERE,null,e);
        } finally {
            
        }
        memberView.getItems().setAll(list);
    }
  
    public static class Member{
        private final SimpleStringProperty id;
        private final SimpleStringProperty name;
        private final SimpleStringProperty mobile;
        private final SimpleStringProperty email;

        public Member(String id, String name, String mobile, String email) {
            this.id = new SimpleStringProperty(id);
            this.name = new SimpleStringProperty(name);
            this.mobile = new SimpleStringProperty(mobile);
            this.email = new SimpleStringProperty(email);
        }

        public String getId() {
            return id.get();
        }

        public String getName() {
            return name.get();
        }

        public String getMobile() {
            return mobile.get();
        }

        public String getEmail() {
            return email.get();
        }
        
        
        
    }
}
