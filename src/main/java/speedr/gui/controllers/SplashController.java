package speedr.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SplashController implements Initializable {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Application started.");
    }

    @FXML
    public void onLoginButtonClick(ActionEvent evt){

        System.out.println("logging in");

        Stage currentStage = (Stage) ((Node)evt.getSource()).getScene().getWindow();

        Parent main = null;

        try {
            main = FXMLLoader.load(getClass().getResource("/fxml/main_window.fxml"));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        Scene s = new Scene(main);
        s.getStylesheets().add("/style/speedr.css");
        currentStage.setScene(s);

    }

}
