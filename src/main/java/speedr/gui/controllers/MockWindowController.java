package speedr.gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import speedr.core.SpeedReadEventPump;
import speedr.core.SpeedReaderStream;
import speedr.sources.email.Email;
import speedr.sources.email.IMAPInbox;

/**
 *
 * Controller for the Speed Reader testing panel.
 *
 */

public class MockWindowController {

    @FXML private Label speedText;

    @FXML
    public void startReader() {

        // get an email

        IMAPInbox inbox = new IMAPInbox("imap.gmail.com", "speedrorg@gmail.com", "speedrspeedr");
        Email e = inbox.getLastMessage();

        // set up a speed reading stream from the email.

        SpeedReaderStream s = new SpeedReaderStream(e);

        // the pump lets us plug the stream into our gui

        SpeedReadEventPump pump = new SpeedReadEventPump(s, 700);

        pump.addWordPumpEventListener(w -> speedText.setText(w.asText()));

        // kick it off

        pump.start();

    }

}
