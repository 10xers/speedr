package speedr.gui.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import speedr.core.config.ConfigurationRepository;
import speedr.core.config.CorruptedConfigException;
import speedr.core.entities.config.Configuration;
import speedr.sources.email.Email;
import speedr.sources.email.EmailInbox;
import speedr.sources.email.IMAPInbox;
import speedr.sources.email.MockInbox;

import javax.mail.AuthenticationFailedException;
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

    private Configuration configuration;

    @FXML private TextField hostInput;
    @FXML private TextField userInput;
    @FXML private PasswordField passInput;
    @FXML private ProgressBar progressBar;
    @FXML private Label errorLabel;
    @FXML private Button loginButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // fill in the values from the configuration

        try {
            configuration = ConfigurationRepository.load();
            hostInput.setText(configuration.getHost());
            userInput.setText(configuration.getUser());
            passInput.setText(configuration.getPassword());
        } catch (IOException | CorruptedConfigException e) {
            Platform.runLater(() -> error(e));
        }

    }

    @FXML
    public void onLoginButtonClick(ActionEvent evt){

        progressBar.setVisible(true);
        loginButton.setDisable(true);
        userInput.setDisable(true);
        passInput.setDisable(true);
        hostInput.setDisable(true);

        clearError();
        new Thread(() -> loadEmails((Stage) ((Node) evt.getSource()).getScene().getWindow())).start();
        new Thread(this::progressUpdater).start();

    }

    // run this on a normal thread
    public void loadEmails(Stage currentStage){

        try {

            EmailInbox inbox;

            // For quick testing, having a host of "debug" will use a mock inbox with the user/pass of
            // speedrorg@gmail.com/speedrspeedr

            if(hostInput.getText().equals("debug")){
                inbox = new MockInbox(hostInput.getText(), userInput.getText(), passInput.getText());
            } else {
                inbox = new IMAPInbox(hostInput.getText(), userInput.getText(), passInput.getText());
            }

            List<Email> emails = inbox.getRecentMessages(30);

            configuration.setHost(hostInput.getText());
            configuration.setUser(userInput.getText());
            configuration.setPassword(passInput.getText());

            ConfigurationRepository.save(configuration);

            Platform.runLater(() -> loadMain(emails, currentStage));

        } catch(Exception e){
            Platform.runLater(() -> error(e));
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

            Parent main;

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main_window.fxml"));
            main = loader.load();
            MainWindowController controller = loader.getController();
            controller.loadWith(emails, userInput.getText());

            Scene s = new Scene(main);
            s.getStylesheets().add("/style/speedr.css");
            currentStage.setScene(s);

        } catch(Exception e){
            error(e);
        }

    }

    private void error(Exception e){

        if(e instanceof AuthenticationFailedException){
            error("Login credentials are incorrect.");
        } else if(e instanceof CorruptedConfigException){
            error("Configuration file has become corrupt.");
        } else {
            error("Unknown error: " + e.getLocalizedMessage());
        }

    }

    // run this on the gui thread
    private void error(String s){

        this.progressBar.setProgress(0.0d);
        this.progressBar.setVisible(false);
        this.loginButton.setDisable(false);
        this.userInput.setDisable(false);
        this.passInput.setDisable(false);
        this.hostInput.setDisable(false);
        this.errorLabel.setVisible(true);
        this.errorLabel.setText(String.format("Error! %s", s));

    }

    private void clearError(){

        errorLabel.setVisible(false);
        errorLabel.setText("");
    }

}
