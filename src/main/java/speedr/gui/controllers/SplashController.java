package speedr.gui.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import speedr.sources.email.Email;
import speedr.sources.email.IMAPInbox;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 *
 * This is the controller for the GUI Splash Screen.
 *
 */

public class SplashController implements Initializable {

    @FXML public TextField hostInput;
    @FXML public TextField userInput;
    @FXML public PasswordField passInput;
    @FXML public ProgressBar progressBar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void onLoginButtonClick(ActionEvent evt){

        progressBar.setVisible(true);
        new Thread(() -> loadEmails((Stage) ((Node) evt.getSource()).getScene().getWindow())).start();
        new Thread(this::progressUpdater).start();

    }

    // run this on a normal thread
    public void loadEmails(Stage currentStage){

        try {

            IMAPInbox inbox = new IMAPInbox(hostInput.getText(), userInput.getText(), passInput.getText());
            List<Email> emails = inbox.getRecentMessages(30);

            Platform.runLater(() -> loadMain(emails, currentStage));

        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    // run this on a normal thread
    public void progressUpdater(){

        for(int i = 0; i < 100; ++i) {
            Platform.runLater(() -> progressBar.setProgress(progressBar.getProgress() + 0.01));
            try {
                Thread.sleep(70);
            } catch (InterruptedException e) {
                // who cares?
            }
        }

    }

    // run this on the gui thread
    public void loadMain(List<Email> emails, Stage currentStage){

        try {

            Parent main = null;

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main_window.fxml"));
            main = loader.load();
            MainWindowController controller = loader.getController();
            controller.loadWith(emails);

            Scene s = new Scene(main);
            s.getStylesheets().add("/style/speedr.css");
            currentStage.setScene(s);

        } catch(Exception e){
            throw new RuntimeException(e);
        }

    }

}
