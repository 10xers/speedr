package speedr.gui.controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import speedr.core.SpeedReadEventPump;
import speedr.core.SpeedReaderStream;
import speedr.core.entities.Context;
import speedr.core.listeners.WordPumpEvent;
import speedr.core.listeners.WordPumpEventListener;
import speedr.sources.email.Email;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static speedr.gui.helpers.Effects.fadeIn;
import static speedr.gui.helpers.Effects.fadeOut;

/**
 *
 * Controller for the Speed Reader main panel GUI.
 *
 */

public class MainWindowController implements WordPumpEventListener, Initializable {


    private SpeedReadEventPump pump;
    private List<Email> emails;

    private boolean startedReading = false;


    @FXML
    public BorderPane readerPane;

    @FXML
    public HBox queuePane;

    @FXML
    public Label contextIn;
    @FXML
    public Label contextOut;

    @FXML
    public Button btnSkipBack;
    @FXML
    public Button btnPause;
    @FXML
    public Button btnSkip;
    @FXML
    private Label currentWordLabel;
    @FXML
    private ListView<Email> itemList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @Override
    public void wordPump(WordPumpEvent wordPumpEvent) {

        if (wordPumpEvent.isDone()) {
            startedReading = false;
        } else {
            currentWordLabel.setText(wordPumpEvent.getWord().asText());
        }

    }


    private void activateReadingMode()
    {
        startedReading = true;

        fadeOut(queuePane, 500);
        fadeIn(readerPane, 500);

        // set up a speed reading stream from the email.
        SpeedReaderStream s = new SpeedReaderStream(
                itemList.getSelectionModel().getSelectedItem(),
                500
        );

        // the pump lets us plug the stream into our gui
        pump = new SpeedReadEventPump(s);
        pump.addWordPumpEventListener(this);

        // wait for the transition, then go

        new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {}

            Platform.runLater(pump::start);
        }).start();

    }


    @FXML
    public void onKeyPressed(KeyEvent keyEvent) {

        if (startedReading)
        {
            switch(keyEvent.getCode())
            {
                case LEFT:
                    hitBack();
                    break;
                case RIGHT:
                    hitSkip();
                    break;
                case UP:
                    hitPause();
                    break;
                default:
                    break;
            }
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

    public void loadWith(List<Email> emails)
    {
        ObservableList<Email> items = FXCollections.observableArrayList();
        items.addAll(emails);

        itemList.setItems(items);

        itemList.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    // do we need to do anything here?
                }
        );


        itemList.setDisable(false);

        }

    private void hitPause()
    {
        Context current;

        try {
            current = pump.pauseAndGetContext();
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }

        String joinedBefore = current.getBefore().stream().map((w)-> w.asText()).collect(Collectors.joining(" "));
        String joinedAfter =  current.getAfter().stream().map((w)-> w.asText()).collect(Collectors.joining(" "));

        contextIn.setText(joinedBefore);
        contextOut.setText(joinedAfter);

        fadeIn(contextIn, 1500);
        fadeIn(contextOut, 1500);
    }

    private void hitSkip()
    {
        if (this.pump.isPaused())
        {
            try {
                this.pump.setPaused(false);
            } catch (InterruptedException e) {
                throw new IllegalStateException("failed to unpause pump", e);
            }

            fadeOut(contextIn, 300);
            fadeOut(contextOut, 300);
        }
    }

    private void hitBack()
    {
        if (!pump.isAtStart()) {

            if (pump.isPaused())
            {
                try {
                    pump.goBackSentenceAndRefire();
                } catch (InterruptedException e) {
                    throw new IllegalStateException(e);
                }
            } else {
                try {
                    pump.goBackWordAndReFire();
                } catch (InterruptedException e) {
                    throw new IllegalStateException(e);
                }
            }
        }

        fadeOut(contextIn, 300);
        fadeOut(contextOut, 300);
    }

    @FXML
    public void handleStreamChangeRequest(ActionEvent actionEvent) {

        if (actionEvent.getSource()== btnPause)
        {
            hitPause();
        }

        if (actionEvent.getSource()==btnSkip)
        {
            hitSkip();
        }

        if (actionEvent.getSource()== btnSkipBack)
        {
            hitBack();
        }

    }

    @FXML
    public void startReadingBtnClicked(ActionEvent evt) {
        activateReadingMode();
    }
}
