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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import speedr.core.SpeedReadEventPump;
import speedr.core.SpeedReaderStream;
import speedr.core.entities.Context;
import speedr.core.entities.Word;
import speedr.core.listeners.WordPumpEvent;
import speedr.core.listeners.WordPumpEventListener;
import speedr.sources.email.Email;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import static speedr.gui.helpers.Effects.fadeIn;
import static speedr.gui.helpers.Effects.fadeOut;

/**
 *
 * Controller for the Speed Reader main panel GUI.
 *
 */

public class MainWindowController implements WordPumpEventListener, Initializable {



    private Logger l = LoggerFactory.getLogger(MainWindowController.class);

    private SpeedReadEventPump pump;
    private List<Email> emails;

    private boolean startedReading = false;

    @FXML
    public Button playReadingBtn;
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
    public Button stopBtn;
    @FXML
    private Label currentWordLabel;
    @FXML
    private ListView<Email> itemList;
    @FXML
    public BorderPane sourcesBox;
    @FXML
    public Label loginName;
    @FXML
    public ImageView configButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        configButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> onConfigureButtonClick(e));

    }

    @Override
    public void wordPump(WordPumpEvent wordPumpEvent) {

        if (wordPumpEvent.isDone()) {
            startedReading = false;
            deactivateReadingMode();
        } else {
            currentWordLabel.setText(wordPumpEvent.getWord().asText());
        }

    }

    private void deactivateReadingMode()
    {
        sourcesBox.setDisable(false);
        fadeIn(queuePane, 500);
        fadeOut(readerPane, 500);

        currentWordLabel.setText("");
        contextIn.setText("");
        contextOut.setText("");

        new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {}

            Platform.runLater(() -> { readerPane.setVisible(false); });
        }).start();

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

        sourcesBox.setDisable(true);

        // wait for the transition, then go

        new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {}

            Platform.runLater(() -> { queuePane.setVisible(false); new Thread(this::beginReading).start(); });
        }).start();

    }

    private void beginReading()
    {
        int countdown = 3;

        do {
            final int nextCount = countdown;
            Platform.runLater(() -> wordPump(new WordPumpEvent(WordPumpEvent.State.IS_MORE, new Word(""+nextCount, 1000))) );
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                l.error("sleep interrupted in countdown", e);
            }
        } while (--countdown>0);

        pump.start();
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
    public void onConfigureButtonClick(MouseEvent evt) {
        Parent root;

        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/config_window.fxml"));
        } catch (IOException e) {
            throw new IllegalStateException("failed loading fxml for config window", e);
        }

        Stage s = new Stage();
        s.getIcons().add(new Image("/icons/glyphicons/glyphicons-281-settings.png"));
        s.setTitle("Configuration");
        s.setScene(new Scene(root));

        s.initModality(Modality.APPLICATION_MODAL);
        s.initOwner(((Node)evt.getSource()).getScene().getWindow() );
        s.show();

    }

    public void loadWith(List<Email> emails, String name)
    {
        ObservableList<Email> items = FXCollections.observableArrayList();
        items.addAll(emails);

        itemList.setItems(items);

        itemList.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        playReadingBtn.setDisable(false);
                    } else {
                        playReadingBtn.setDisable(true);
                    }
                }
        );

        loginName.setText("@"+name.split("@")[0]);

        sourcesBox.setDisable(false);

        }

    private void hitPause()
    {
        Context current;

        if (!pump.isPaused()) {
            try {
                current = pump.pauseAndGetContext();
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }

            String joinedBefore = current.getBefore().stream().map((w) -> w.asText()).collect(Collectors.joining(" "));
            String joinedAfter = current.getAfter().stream().map((w) -> w.asText()).collect(Collectors.joining(" "));

            contextIn.setText(joinedBefore);
            contextOut.setText(joinedAfter);

            fadeIn(contextIn, 1500);
            fadeIn(contextOut, 1500);

            this.btnPause.setGraphic(new ImageView("/icons/glyphicons/glyphicons-174-play.png"));
        } else {

            try {
                this.pump.setPaused(false);
            } catch (InterruptedException e) {
                throw new IllegalStateException("failed to unpause pump", e);
            }

            fadeOut(contextIn, 300);
            fadeOut(contextOut, 300);
            this.btnPause.setGraphic(new ImageView("/icons/glyphicons/glyphicons-175-pause.png"));
        }
    }

    private void hitSkip()
    {
        if (this.pump.isPaused())
        {

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

        if (actionEvent.getSource()==btnPause)
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

        if (actionEvent.getSource() == stopBtn)
        {
            pump.stop();
            deactivateReadingMode();
        }

    }

    @FXML
    public void startReadingBtnClicked(ActionEvent evt) {
        activateReadingMode();
    }

    public void filterTextChanged(ActionEvent actionEvent) {
        //.. todo filter
    }
}
