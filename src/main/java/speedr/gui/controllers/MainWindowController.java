package speedr.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import speedr.core.SpeedReadEventPump;
import speedr.core.SpeedReaderStream;
import speedr.core.listeners.WordPumpEvent;
import speedr.core.listeners.WordPumpEventListener;
import speedr.sources.email.Email;
import speedr.sources.email.IMAPInbox;

/**r
 *
 * Controller for the Speed Reader main panel GUI.
 *
 */

public class MainWindowController implements WordPumpEventListener {

    private SpeedReadEventPump pump;

    private boolean startedReading = false;

    @FXML
    private Label promptLabel;

    @FXML
    private Label currentWordLabel;

    @FXML
    private Text finishText;

    @FXML
    public void onKeyPressed(KeyEvent keyEvent) {

        if (!startedReading && (keyEvent.getCode() == KeyCode.P)) {

            startedReading = true;

            promptLabel.setVisible(false);
            currentWordLabel.setVisible(true);

            // get an email
            IMAPInbox inbox = new IMAPInbox("imap.gmail.com", "speedrorg@gmail.com", "speedrspeedr");
            Email e = inbox.getLastMessage();

            // set up a speed reading stream from the email.
            SpeedReaderStream s = new SpeedReaderStream(e);

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
}
