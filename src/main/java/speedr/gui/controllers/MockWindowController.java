package speedr.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import speedr.core.SpeedReadEventPump;
import speedr.core.SpeedReaderStream;
import speedr.core.WordPumpEventListener;
import speedr.core.entities.Word;
import speedr.core.sources.email.Email;
import speedr.core.sources.email.IMAPInbox;

/**
 *
 * Controller for the Speed Reader testing panel.
 *
 */


public class MockWindowController {

    @FXML private Label speedText;

    @FXML
    public void startReader(ActionEvent event) {

        // get an email

        IMAPInbox inbox = new IMAPInbox("imap.gmail.com", "speedrorg@gmail.com", "speedrspeedr");
        Email e = inbox.getLastMessage();

        // set up a speed reading stream from the email.

        SpeedReaderStream s = new SpeedReaderStream(e);

        // the pump lets us plug the stream into our gui

        SpeedReadEventPump pump = new SpeedReadEventPump(s, 700);

        pump.addWordPumpEventListener(new WordPumpEventListener() {
            @Override
            public void wordPump(Word w) {
                speedText.setText(w.asText());
            }
        });

        // kick it off

        pump.start();

    }

}
