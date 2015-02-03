package speedr.gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import speedr.core.SpeedReadEventPump;
import speedr.core.SpeedReaderStream;
import speedr.core.WordPumpEventListener;
import speedr.core.entities.Word;

public class MockWindowController {

    @FXML private Label speedText;

    @FXML
    public void startReader(ActionEvent event) {

        SpeedReaderStream s = new SpeedReaderStream(
                () -> "Do you ever feel like a plastic bag. Drifting through the wind, wanting to start again?"
        );

        SpeedReadEventPump pump = new SpeedReadEventPump(s, 700);

        pump.addWordPumpEventListener(new WordPumpEventListener() {
            @Override
            public void wordPump(Word w) {
                System.out.println("Pumping: " + w.asText());
                speedText.setText(w.asText());
            }
        });

        pump.start();


    }

}
