/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library.assistant.allerts;

import javafx.scene.control.Alert;

/**
 *
 * @author SAYYAD
 */
public class MakeAlert {
    public static void showInforamtionAlert(String title,String Content) {
        Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
        alert1.setTitle(title);
        alert1.setHeaderText(null);
        alert1.setContentText(Content);
        alert1.showAndWait();
    }
    
    public static void showWrrorAlert(String title,String Content) {
        Alert alert1 = new Alert(Alert.AlertType.ERROR);
        alert1.setTitle(title);
        alert1.setHeaderText(null);
        alert1.setContentText(Content);
        alert1.showAndWait();
    }

}
