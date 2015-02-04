package speedr.gui.controllers;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import speedr.core.SpeedReadEventPump;
import speedr.core.SpeedReaderStream;
import speedr.core.listeners.WordPumpEvent;
import speedr.core.listeners.WordPumpEventListener;
import speedr.sources.email.Email;
import speedr.sources.email.IMAPInbox;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**r
 *
 * Controller for the Speed Reader main panel GUI.
 *
 */

public class MainWindowController implements WordPumpEventListener, Initializable {

    private SpeedReadEventPump pump;
    private List<Email> emails;

    private boolean startedReading = false;

    @FXML
    private Label promptLabel;

    @FXML
    private Label currentWordLabel;

    @FXML
    private ListView<Email> itemList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new Thread(this::loadEmails).start();
    }

    @Override
    public void wordPump(WordPumpEvent wordPumpEvent) {

        if (wordPumpEvent.isDone()) {
            startedReading = false;
        } else {
            currentWordLabel.setText(wordPumpEvent.getWord().asText());
        }

    }

    @FXML
    public void onKeyPressed(KeyEvent keyEvent) {

        if (!startedReading && (keyEvent.getCode() == KeyCode.P) && itemList.getSelectionModel().getSelectedItem() != null) {

            startedReading = true;

            promptLabel.setVisible(false);

            // set up a speed reading stream from the email.
            SpeedReaderStream s = new SpeedReaderStream(
                itemList.getSelectionModel().getSelectedItem()
            );

            // the pump lets us plug the stream into our gui
            pump = new SpeedReadEventPump(s, 700);
            pump.addWordPumpEventListener(this);

            // kick it off
            pump.start();

        } else if(startedReading && keyEvent.getCode() == KeyCode.P) {

            // they hit p while we were reading, stop the pump.

            pump.removeWordPumpEventListener(this);

            currentWordLabel.setText("");
            setPrompt("Select an email and press P to speed read. Press P again to stop.");

            startedReading = false;

        }
    }

    @FXML
    public void onConfigureButtonClick(ActionEvent evt) {
        Parent root;

        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/config_window.fxml"));
        } catch (IOException e) {
            throw new IllegalStateException("failed loading fxml for config window", e);
        }

        Stage s = new Stage();
        s.setTitle("Configuration");
        s.setScene(new Scene(root));

        s.initModality(Modality.APPLICATION_MODAL);
        s.initOwner(((Node)evt.getSource()).getScene().getWindow() );
        s.show();

    }

    private void loadEmails() {

        setPrompt("Loading emails...");

        IMAPInbox inbox = new IMAPInbox("imap.gmail.com", "speedrorg@gmail.com", "speedrspeedr");
        emails = inbox.getRecentMessages(30);

        ObservableList<Email> items = FXCollections.observableArrayList();
        items.addAll(emails);

        itemList.setItems(items);

        itemList.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                System.out.println("New email selected:");
            }
        );

        Platform.runLater(() -> {
            setPrompt("Select an email and press P to speed read. Press P again to stop.");
            currentWordLabel.setVisible(true);
        });
    }

    private void setPrompt(String text){
        promptLabel.setVisible(true);
        promptLabel.setText(text);
    }
}
