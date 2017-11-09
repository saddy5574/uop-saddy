package library.assistant.ui.listbook;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleBooleanProperty;
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

public class FXMLListBookController implements Initializable {

    ObservableList<Book> list  = FXCollections.observableArrayList();
    
    @FXML
    private AnchorPane rootPane;
    @FXML
    private TableView<Book> bookView;
    @FXML
    private TableColumn<Book, String> idCol;
    @FXML
    private TableColumn<Book, String> titleCol;
    @FXML
    private TableColumn<Book, String> authorCol;
    @FXML
    private TableColumn<Book, String> publisherCol;
    @FXML
    private TableColumn<Book, Boolean> availabilityCol;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initCol();
        loadData();
    }    

    private void initCol() {
        titleCol.setCellValueFactory(new PropertyValueFactory<>("tilte"));
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        publisherCol.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        availabilityCol.setCellValueFactory(new PropertyValueFactory<>("availability"));
        
    }

    private void loadData() {
        DatabaseHandler databaseHandler = DatabaseHandler.getIntance();
        String qu = "SELECT * FROM BOOK";
        ResultSet resultSet = databaseHandler.execQuery(qu);
        
        try {
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                String publisher = resultSet.getString("publisher");
                Boolean availability = resultSet.getBoolean("isAvail");
                
                list.add(new Book(id, title, author, publisher, availability));
            }
        } catch (SQLException e) {
            Logger.getLogger(FXMLAddBookController.class.getName()).log(Level.SEVERE,null,e);
        } finally {
            
        }
        bookView.getItems().setAll(list);
    }
    
    public static class Book{
        private final SimpleStringProperty id;
        private final SimpleStringProperty tilte;
        private final SimpleStringProperty author;
        private final SimpleStringProperty publisher;
        private final SimpleBooleanProperty availability;

        public Book(String id, String tilte, String author, String publisher, Boolean availability) {
            this.id = new SimpleStringProperty(id);
            this.tilte = new SimpleStringProperty(tilte);
            this.author = new SimpleStringProperty(author);
            this.publisher = new SimpleStringProperty(publisher);
            this.availability = new SimpleBooleanProperty(availability) ;
        }

        public String getId() {
            return id.get();
        }

        public String getTilte() {
            return tilte.get();
        }

        public String getAuthor() {
            return author.get();
        }

        public String getPublisher() {
            return publisher.get();
        }

        public Boolean getAvailability() {
            return availability.get();
        }
    }
}
