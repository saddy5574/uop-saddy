package library.assistant.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class DatabaseHandler {
    
    private static DatabaseHandler handler = null;
    
    private static final String DB_URL = "jdbc:derby:dataabse;create=true";
    private static Connection conn = null;
    private static Statement stmt = null;
    
    private DatabaseHandler(){
        createConnection();
        setupBookTable();
        setupMemberTable();
        setupIssueTable();
    }
    
    public static DatabaseHandler getIntance() {
        if (handler == null) {
            handler = new DatabaseHandler();
        }
        return handler;
    }
    
    void createConnection(){
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
            conn = DriverManager.getConnection(DB_URL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    void setupBookTable(){
        String TABLE_NAME = "BOOK";
        
        try {
            stmt = (Statement) conn.createStatement();
            
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null,null,TABLE_NAME.toUpperCase(), null);
            
            if (tables.next()) {
                System.out.println("Table " + TABLE_NAME + " already exist ready for go!");
            } else {
                stmt.execute("CREATE TABLE " + TABLE_NAME + "("
                        + "     id varchar(200) primary key,\n"
                        + "     title varchar(200),\n"
                        + "     author varchar(200),\n"
                        + "     publisher varchar(100),\n"
                        + "     isAvail boolean default true"
                        + " )");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage() + " ----- setupDatabase");
        }finally {
        }
    }
    
    private void setupMemberTable() {
        String TABLE_NAME = "MEMBER";
        
        try {
            stmt = (Statement) conn.createStatement();
            
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null,null,TABLE_NAME.toUpperCase(), null);
            
            if (tables.next()) {
                System.out.println("Table " + TABLE_NAME + " already exist ready for go!");
            } else {
                stmt.execute("CREATE TABLE " + TABLE_NAME + "("
                        + "     id varchar(200) primary key,\n"
                        + "     name varchar(200),\n"
                        + "     mobile varchar(200),\n"
                        + "     email varchar(100)"
                        + " )");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage() + " ----- setupDatabase");
        }finally {
        }
    }
    
    private void setupIssueTable() {
        String TABLE_NAME = "ISSUE";

        try {
            stmt = (Statement) conn.createStatement();

            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(), null);

            if (tables.next()) {
                System.out.println("Table " + TABLE_NAME + " already exist ready for go!");
            } else {
                stmt.execute("CREATE TABLE " + TABLE_NAME + "("
                        + "     bookId varchar(200),\n"
                        + "     memberId varchar(200),\n"
                        + "     issueTime timestamp default CURRENT_TIMESTAMP,\n"
                        + "     renewCount integer default 0,\n"
                        + "     PRIMARY KEY (bookId,memberId),\n"
                        + "     FOREIGN KEY (bookId) REFERENCES BOOK(id),\n"
                        + "     FOREIGN KEY (memberId) REFERENCES MEMBER(id)"
                        + " )");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage() + " ----- setupDatabase");
        } finally {
        }
    }
    

    
    public ResultSet execQuery(String query){
        ResultSet resultSet;
        try {
            stmt = conn.createStatement();
            resultSet = stmt.executeQuery(query);
        } catch (SQLException e) {
            System.out.println("Exception at execQuery:databaseHandler"+e.getLocalizedMessage());
            return null;
        }finally {
        }
        return resultSet;
    }
    
    public boolean execAction(String query){
        ResultSet resultSet;
        try {
            stmt = conn.createStatement();
            stmt.execute(query);
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error:" + e.getMessage(), "Error Occured", JOptionPane.ERROR_MESSAGE);
            System.out.println("Exception at execAction:databaseHandler"+e.getLocalizedMessage());
            return false;
        }finally {
        }
    }     

}
