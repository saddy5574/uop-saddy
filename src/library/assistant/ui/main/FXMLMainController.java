package library.assistant.ui.main;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.effects.JFXDepthManager;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import library.assistant.database.DatabaseHandler;
import library.assistant.ui.addbook.FXMLAddBookController;

public class FXMLMainController implements Initializable {

    @FXML
    private HBox bookInfo;
    @FXML
    private HBox memberInfo;
    @FXML
    private JFXTextField bookIdInput;
    @FXML
    private Text bookName;
    @FXML
    private Text bookAuthor;
    @FXML
    private Text bookAvailabilty;
    @FXML
    private JFXTextField memberIdInput;
    @FXML
    private Text memberName;
    @FXML
    private Text memberContact;

    DatabaseHandler databaseHandler = DatabaseHandler.getIntance();
    Boolean isBookReadyForSubmission;

    @FXML
    private JFXTextField bookIdForIssue;
    @FXML
    private ListView<String> issuedBookList;

    public FXMLMainController() {
        this.isBookReadyForSubmission = false;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        JFXDepthManager.setDepth(bookInfo, 1);
        JFXDepthManager.setDepth(memberInfo, 1);
    }

    @FXML
    private void loadAddMember(ActionEvent event) {
        loadWindow("/library/assistant/ui/addmember/FXMLAddMember.fxml", "Add New Member");
    }

    @FXML
    private void loadAddBook(ActionEvent event) {
        loadWindow("/library/assistant/ui/addbook/FXMLAddBook.fxml", "Add New Book");
    }

    @FXML
    private void loadMemberTable(ActionEvent event) {
        loadWindow("/library/assistant/ui/listmember/FXMLListMember.fxml", "List of all Members");
    }

    @FXML
    private void loadBookTable(ActionEvent event) {
        loadWindow("/library/assistant/ui/listbook/FXMLListBook.fxml", "List of all Books");
    }
    
    @FXML
    private void loadSetting(ActionEvent event) {
        loadWindow("/library/assistant/settings/FXMLSettings.fxml", "Settings");
    }
    
    void loadWindow(String loc, String title) {
        Parent parent;
        try {
            parent = FXMLLoader.load(getClass().getResource(loc));
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.show();

        } catch (IOException ex) {
            Logger.getLogger(FXMLMainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void loadBookInfo(ActionEvent event) {
        clearBookInfoCache();

        String id = bookIdInput.getText().toLowerCase();
        String qu = "SELECT * FROM BOOK WHERE id = '" + id + "'";
        ResultSet resultSet = databaseHandler.execQuery(qu);

        try {
            while (resultSet.next()) {
                String bName = resultSet.getString("title");
                String bAuthor = resultSet.getString("author");
                Boolean bStatus = resultSet.getBoolean("isAvail");

                bookName.setText(bName);
                bookAuthor.setText(bAuthor);
                String status = (bStatus) ? "Available" : "Not Available";
                bookAvailabilty.setText(status);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void loadMemberInfo(ActionEvent event) {
        clearMemberInfoCache();

        String id = memberIdInput.getText().toLowerCase();
        String qu = "SELECT * FROM MEMBER WHERE id = '" + id + "'";
        ResultSet resultSet = databaseHandler.execQuery(qu);

        try {
            while (resultSet.next()) {
                String bName = resultSet.getString("name");
                String bContact = resultSet.getString("mobile");

                memberName.setText(bName);
                memberContact.setText(bContact);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearBookInfoCache() {
        bookName.setText("");
        bookAuthor.setText("");
        bookAvailabilty.setText("");
    }

    private void clearMemberInfoCache() {
        memberName.setText("");
        memberContact.setText("");
    }

    @FXML
    private void loadIssueOperation(ActionEvent event) {
        String memberId = memberIdInput.getText().toLowerCase();
        String bookId = bookIdInput.getText().toLowerCase();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Issue Operation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure want to issue the book " + bookName.getText() + "\n to " + memberName.getText() + " ?");

        Optional<ButtonType> responce = alert.showAndWait();

        if (responce.get() == ButtonType.OK) {
            String str1 = "INSERT INTO ISSUE(bookId,memberId) VALUES ( "
                    + "'" + bookId + "',"
                    + "'" + memberId + "')";
            String str2 = "UPDATE BOOK SET isAvail = false WHERE id = '" + bookId + "'";

            // Check book Availability.
            if (!checkAvailiblity(bookId)) {
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Cancelled");
                alert1.setHeaderText(null);
                alert1.setContentText("Issue Operation Cancelled because book is not available.");
                alert1.showAndWait();
                return;
            } else {
                if (databaseHandler.execAction(str1) && databaseHandler.execAction(str2)) {
                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                    alert1.setTitle("Success");
                    alert1.setHeaderText(null);
                    alert1.setContentText("Book Issue Complete");
                    alert1.showAndWait();
                } else {
                    Alert alert1 = new Alert(Alert.AlertType.ERROR);
                    alert1.setTitle("Failed");
                    alert1.setHeaderText(null);
                    alert1.setContentText("Issue Operation Failed");
                    alert1.showAndWait();
                }
            }
        } else {
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setTitle("Cancelled");
            alert1.setHeaderText(null);
            alert1.setContentText("Issue Operation Cancelled");
            alert1.showAndWait();
        }
    }

    public Boolean checkAvailiblity(String Id) {
        String qu = "SELECT isAvail FROM BOOK WHERE id = '" + Id + "'";
        ResultSet resultSet = databaseHandler.execQuery(qu);
        Boolean availability = null;
        try {
            while (resultSet.next()) {
                availability = resultSet.getBoolean("isAvail");
            }
        } catch (SQLException e) {
            Logger.getLogger(FXMLAddBookController.class.getName()).log(Level.SEVERE, null, e);
        } finally {

        }

        return availability;
    }

    @FXML
    private void loadIssuedBookInfo(ActionEvent event) {
        isBookReadyForSubmission = false;
        ObservableList<String> issuedData = FXCollections.observableArrayList();
        String id = bookIdForIssue.getText().toLowerCase();

        String qu = "SELECT * FROM ISSUE WHERE bookId = '" + id + "'";
        ResultSet resultSet = databaseHandler.execQuery(qu);

        try {
            while (resultSet.next()) {
                String mBookId = id;
                String mMemberId = resultSet.getString("memberId");
                Timestamp mIssueTime = resultSet.getTimestamp("issueTime");
                String mRenewCount = resultSet.getString("renewCount");

                issuedData.add("Date , Time and Renew Count :- ");
                issuedData.add("    Issue Date and Time : " + mIssueTime.toGMTString());
                issuedData.add("    Renew Count : " + mRenewCount);

                issuedData.add("Book Information :- ");
                qu = "SELECT * FROM BOOK WHERE id = '" + mBookId + "'";
                ResultSet resultSet1 = databaseHandler.execQuery(qu);
                while (resultSet1.next()) {
                    issuedData.add("    Book Name : " + resultSet1.getString("title"));
                    issuedData.add("    Book ID : " + resultSet1.getString("id"));
                    issuedData.add("    Book Author : " + resultSet1.getString("author"));
                    issuedData.add("    Book Publisher : " + resultSet1.getString("publisher"));
                }

                issuedData.add("Book Information :- ");
                qu = "SELECT * FROM MEMBER WHERE id = '" + mMemberId + "'";
                resultSet1 = databaseHandler.execQuery(qu);
                while (resultSet1.next()) {
                    issuedData.add("    Member Name : " + resultSet1.getString("name"));
                    issuedData.add("    Member ID : " + resultSet1.getString("id"));
                    issuedData.add("    Mobile : " + resultSet1.getString("mobile"));
                    issuedData.add("    Email : " + resultSet1.getString("email"));
                }
                isBookReadyForSubmission = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(FXMLMainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        issuedBookList.getItems().setAll(issuedData);
    }

    @FXML
    private void loadSubmissionOperation(ActionEvent event) {
        if (!isBookReadyForSubmission) {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setTitle("Failed");
            alert1.setHeaderText(null);
            alert1.setContentText("Please select book for Submission");
            alert1.showAndWait();
            return;
        }

        String bookId = bookIdForIssue.getText().toLowerCase();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Submission Operation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure want to Submit the book ?");

        Optional<ButtonType> responce = alert.showAndWait();

        if (responce.get() == ButtonType.OK) {
            String str1 = "DELETE FROM ISSUE WHERE bookId = '" + bookId + "'";
            String str2 = "UPDATE BOOK SET isAvail = true WHERE id = '" + bookId + "'";

            if (databaseHandler.execAction(str1) && databaseHandler.execAction(str2)) {
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Success");
                alert1.setHeaderText(null);
                alert1.setContentText("Book Submission Complete");
                alert1.showAndWait();
            } else {
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle("Failed");
                alert1.setHeaderText(null);
                alert1.setContentText("Book Submission Failed");
                alert1.showAndWait();
            }
        } else {
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setTitle("Cancelled");
            alert1.setHeaderText(null);
            alert1.setContentText("Submission Operation Cancelled");
            alert1.showAndWait();
        }
    }

    @FXML
    private void loadRenewOperation(ActionEvent event) {
        if (!isBookReadyForSubmission) {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setTitle("Failed");
            alert1.setHeaderText(null);
            alert1.setContentText("Please select book for Renew");
            alert1.showAndWait();
            return;
        }

        String bookId = bookIdForIssue.getText().toLowerCase();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Renew Operation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure want to Renew the book ?");

        Optional<ButtonType> responce = alert.showAndWait();

        if (responce.get() == ButtonType.OK) {
            String str1 = "UPDATE ISSUE SET issueTime = CURRENT_TIMESTAMP, renewCount = renewCount+1 WHERE bookId = '" + bookId + "'";

            if (databaseHandler.execAction(str1)) {
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Success");
                alert1.setHeaderText(null);
                alert1.setContentText("Book has beeb Renewed");
                alert1.showAndWait();
            } else {
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setTitle("Failed");
                alert1.setHeaderText(null);
                alert1.setContentText("Book Renew Operation Failed");
                alert1.showAndWait();
            }
        } else {
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setTitle("Cancelled");
            alert1.setHeaderText(null);
            alert1.setContentText("Renew Operation Cancelled");
            alert1.showAndWait();
        }
    }
}
