package speedr.gui.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import speedr.core.SpeedReadEventPump;
import speedr.core.SpeedReaderStream;
import speedr.core.listeners.WordPumpEvent;
import speedr.core.listeners.WordPumpEventListener;
import speedr.sources.email.Email;
import speedr.sources.email.IMAPInbox;

/**
 *
 * Controller for the Speed Reader testing panel.
 *
 */

public class MockWindowController implements WordPumpEventListener {

    @FXML private Label speedText;
    @FXML private Text fintext;

    private SpeedReadEventPump pump;

    @FXML
    public void startReader() {

        // get an email

        //IMAPInbox inbox = new IMAPInbox("imap.gmail.com", "speedrorg@gmail.com", "speedrspeedr");
        //Email e = inbox.getLastMessage();

        // set up a speed reading stream from the email.

        SpeedReaderStream s = new SpeedReaderStream(()->"WAR IS PEACE\n" +
                                                        "FREEDOM IS SLAVERY\n" +
                                                        "IGNORANCE IS STRENGTH\n");

        // the pump lets us plug the stream into our gui

         pump = new SpeedReadEventPump(s, 700);

        pump.addWordPumpEventListener(this);

        // kick it off

        pump.start();

    }

    @Override
    public void wordPump(WordPumpEvent wordPumpEvent) {
        if (wordPumpEvent.isDone())
        {
            // pump.removeWordPumpEventListener(this);
            // annoyingly can't do this because we're using the listener list to do the iteration for this callback
            // unless i take a copy
            // TBC
            fintext.setVisible(true);
        } else {
            speedText.setText(wordPumpEvent.getWord().asText());
        }
    }
}
