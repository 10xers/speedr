package speedr.gui.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import speedr.core.SpeedReadEventPump;
import speedr.core.SpeedReaderStream;
import speedr.core.listeners.WordPumpEvent;
import speedr.core.listeners.WordPumpEventListener;
import speedr.sources.email.Email;
import speedr.sources.email.IMAPInbox;

import java.net.URL;
import java.util.ResourceBundle;

/**r
 *
 * Controller for the Speed Reader main panel GUI.
 *
 */

public class MainWindowController implements WordPumpEventListener, Initializable {

    private SpeedReadEventPump pump;
    private Email currentEmail;

    private boolean startedReading = false;

    @FXML
    private Label loadingLabel;

    @FXML
    private Label promptLabel;

    @FXML
    private Label currentWordLabel;

    @FXML
    private Text finishText;

    @FXML
    private ListView itemList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new Thread(this::loadEmails).start();
    }

    @Override
    public void wordPump(WordPumpEvent wordPumpEvent) {
        if (wordPumpEvent.isDone())
        {
            // pump.removeWordPumpEventListener(this);
            // annoyingly can't do this because we're using the listener list to do the iteration for this callback
            // unless i take a copy
            // TBC
            finishText.setVisible(true);
        } else {
            currentWordLabel.setText(wordPumpEvent.getWord().asText());
        }
    }

    @FXML
    public void onKeyPressed(KeyEvent keyEvent) {

        if (!startedReading && (keyEvent.getCode() == KeyCode.P)) {

            startedReading = true;

            promptLabel.setVisible(false);
            currentWordLabel.setVisible(true);

            // set up a speed reading stream from the email.
            SpeedReaderStream s = new SpeedReaderStream(currentEmail);

            // the pump lets us plug the stream into our gui
            pump = new SpeedReadEventPump(s, 700);
            pump.addWordPumpEventListener(this);

            // kick it off
            pump.start();
        }
    }

    @FXML
    public void onConfigureButtonClick() {

        //ToDo: Settings
    }

    private void loadEmails() {

        IMAPInbox inbox = new IMAPInbox("imap.gmail.com", "speedrorg@gmail.com", "speedrspeedr");
        currentEmail = inbox.getLastMessage();

        ObservableList<String> items = FXCollections.observableArrayList(
            currentEmail.getFrom()+"\n  "+ currentEmail.getSubject()
        );

        itemList.setItems(items);

        Platform.runLater(() -> {
            loadingLabel.setVisible(false);
            promptLabel.setVisible(true);
        });
    }
}
