package library.assistant.settings;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.digest.DigestUtils;

public class Preferences {
    
    public static final String CONFIG_FILE = "config.txt"; 
    
    private int nDaysWithoutFine;
    private float finePerDay;
    private String userName;
    private String password;

    public Preferences() {
        this.nDaysWithoutFine = 14;
        this.finePerDay = 2.0f;
        this.userName = "admin";
        setPassword("admin");
    }

    public int getnDaysWithoutFine() {
        return nDaysWithoutFine;
    }

    public void setnDaysWithoutFine(int nDaysWithoutFine) {
        this.nDaysWithoutFine = nDaysWithoutFine;
    }

    public float getFinePerDay() {
        return finePerDay;
    }

    public void setFinePerDay(float finePerDay) {
        this.finePerDay = finePerDay;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password.length() < 16) {
            this.password = DigestUtils.shaHex(password);
        } else {
            this.password = password;
        }
    }

    
    
    public static void initConfig() throws IOException {
        Writer writer = null;
        try {
            Preferences preferences = new Preferences();
            Gson gson = new GsonBuilder().create();
            writer = new FileWriter(CONFIG_FILE);
            gson.toJson(preferences, writer);
        } catch (IOException ex) {
            Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            writer.close();
        }
    }
    
    public static Preferences getPreferences() throws IOException {
        Preferences preferences = new Preferences();
        Gson gson = new GsonBuilder().create();
        
        try {
            preferences = gson.fromJson(new FileReader(CONFIG_FILE), Preferences.class );
        } catch (FileNotFoundException ex) {
            initConfig();
            Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
           
        }
        return preferences;
    }
    
    public static void writePrefrencesToFile(Preferences preferences) throws IOException{
        Writer writer = null;
        try {
            Gson gson = new GsonBuilder().create();
            writer = new FileWriter(CONFIG_FILE);
            gson.toJson(preferences, writer);
        } catch (IOException ex) {
            Logger.getLogger(Preferences.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            writer.close();
        }
    }
}
