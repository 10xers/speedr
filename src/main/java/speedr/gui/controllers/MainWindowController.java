package speedr.gui.controllers;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import speedr.core.SpeedReadEventPump;
import speedr.core.SpeedReaderStream;
import speedr.core.entities.Context;
import speedr.core.entities.Word;
import speedr.core.listeners.WordPumpEvent;
import speedr.core.listeners.WordPumpEventListener;
import speedr.gui.controls.EmailListCell;
import speedr.gui.helpers.Filters;
import speedr.gui.helpers.WordRender;
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

    private boolean startedReading = false;

    @FXML
    private TextField filterText;
    @FXML
    private Button btnPlayNextYes;
    @FXML
    private Button btnPlayNextNo;
    @FXML
    private HBox playNextBox;
    @FXML
    private HBox streamControlBar;
    @FXML
    private Button playReadingBtn;
    @FXML
    private BorderPane readerPane;
    @FXML
    private HBox queuePane;
    @FXML
    private Label contextIn;
    @FXML
    private Label contextOut;
    @FXML
    private Button btnSkipBack;
    @FXML
    private Button btnPause;
    @FXML
    private Button btnSkip;
    @FXML
    private Button stopBtn;
    @FXML
    private TextFlow currentWordLabel;
    @FXML
    private ListView<Email> itemList;
    @FXML
    private BorderPane sourcesBox;
    @FXML
    private Label loginName;
    @FXML
    private ImageView configButton;
    @FXML
    private Slider wpmSlider;
    @FXML
    private Label wpmLabel;
    @FXML
    private Label errorBox;

    private boolean stopOrdered = false;
    private int wpm = 700;

    private List<Email> origEmails;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        configButton.addEventHandler(MouseEvent.MOUSE_CLICKED, this::onConfigureButtonClick);

        wpmSlider.valueProperty().addListener(new ChangeListener<Number>() {

            private String[] types = {"Slow", "Easy", "Normal", "Fast", "Fwooosh"};
            private int[] wpms = {200, 300, 400, 700, 1000};

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                wpmSlider.setValue(Math.round(observable.getValue().floatValue()));
                wpmLabel.setText(types[observable.getValue().intValue()]);
                wpm = wpms[observable.getValue().intValue()];
            }

        });

        Callback<ListView<Email>, ListCell<Email>> cellFactory = listView -> new EmailListCell();

        itemList.setCellFactory(cellFactory);

        itemList.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        playReadingBtn.setDisable(false);
                    } else {
                        playReadingBtn.setDisable(true);
                    }
                }
        );

        Platform.runLater(itemList::requestFocus);

    }

    @Override
    public void wordPump(WordPumpEvent wordPumpEvent) {

        if (wordPumpEvent.isDone()) {
            startedReading = false;
            if (stopOrdered)
            {
                deactivateReadingMode();
            } else {
                activatePlayNextScreen();
            }
            stopOrdered=false;
        } else {
            WordRender.renderWordInto(wordPumpEvent.getWord().asText(), currentWordLabel);
        }

    }

    private void activatePlayNextScreen()
    {
        fadeOut(readerPane, 500);
        fadeIn(playNextBox, 500);

        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                Platform.runLater(()->{
                    readerPane.setVisible(false);
                    playNextBox.setVisible(true);});
            }
        }, 500);
    }

    private void deactivateReadingMode()
    {
        sourcesBox.setDisable(false);

        fadeIn(queuePane, 500);
        fadeOut(readerPane, 500);
        fadeOut(playNextBox, 500);

        WordRender.clear(currentWordLabel);
        contextIn.setText("");
        contextOut.setText("");

        new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) {}

            Platform.runLater(() -> { queuePane.setVisible(true); playNextBox.setVisible(false); readerPane.setVisible(false);  });
        }).start();

    }

    private void activateReadingMode()
    {
        fadeOut(queuePane, 500);
        fadeIn(readerPane, 500);

        beginReading();
    }

    private void beginReading()
    {
        startedReading = true;

        if (pump!=null)
        {
            pump.stop();
            pump.removeWordPumpEventListener(this);
            pump=null;

            contextIn.setVisible(false);
            contextOut.setVisible(false);
            WordRender.clear(currentWordLabel);
        }

        // set up a speed reading stream from the email.
        SpeedReaderStream s = new SpeedReaderStream(
                itemList.getSelectionModel().getSelectedItem(),
                wpm
        );

        // the pump lets us plug the stream into our gui
        pump = new SpeedReadEventPump(s);
        pump.addWordPumpEventListener(this);

        sourcesBox.setDisable(true);

        // wait for the transition, then go

        new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ignored) {}

            Platform.runLater(() -> { queuePane.setVisible(false); new Thread(this::startCountdownAndStream).start(); });
        }).start();
    }

    private void startCountdownAndStream()
    {
        int countdown = 3;

        do {
            final int nextCount = countdown;
            Platform.runLater(() -> wordPump(new WordPumpEvent(WordPumpEvent.State.IS_MORE, new Word(""+nextCount, 1000))) );
            try {
                Thread.sleep(750);
            } catch (InterruptedException e) {
                l.error("sleep interrupted in countdown", e);
                error("Sleep interrupted in countdown.");
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
            error("failed loading fxml for config window.");
            throw new IllegalStateException("failed loading fxml for config window", e);
        }

        Stage s = new Stage();
        s.getIcons().add(new Image("/icons/glyphicons/glyphicons-281-settings.png"));
        s.setTitle("Configuration");

        Scene scene = new Scene(root);
        scene.getStylesheets().add("/style/speedr.css");
        s.setScene(scene);

        s.initModality(Modality.APPLICATION_MODAL);
        s.initOwner(((Node)evt.getSource()).getScene().getWindow() );
        s.show();

    }

    public void loadWith(List<Email> emails, String name)
    {

        origEmails = emails;
        ObservableList<Email> items = FXCollections.observableArrayList();
        items.addAll(emails);

        itemList.setItems(items);

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
                error("failed to get pause and get pump context.");
                throw new IllegalStateException(e);
            }

            String joinedBefore = current.getBefore().stream().map(Word::asText).collect(Collectors.joining(" "));
            String joinedAfter = current.getAfter().stream().map(Word::asText).collect(Collectors.joining(" "));

            contextIn.setText(joinedBefore);
            contextOut.setText(joinedAfter);

            fadeIn(contextIn, 1500);
            fadeIn(contextOut, 1500);

            this.btnPause.setGraphic(new ImageView("/icons/glyphicons/glyphicons-174-play.png"));
        } else {

            try {
                this.pump.setPaused(false);
            } catch (InterruptedException e) {
                error("failed to unpause pump.");
                throw new IllegalStateException("failed to unpause pump", e);
            }

            fadeOut(contextIn, 300);
            fadeOut(contextOut, 300);
            this.btnPause.setGraphic(new ImageView("/icons/glyphicons/glyphicons-175-pause.png"));
        }
    }

    private void hitSkip()
    {
        this.pump.stop();
        this.sourcesBox.setDisable(false);
        this.itemList.getSelectionModel().selectNext();
        this.sourcesBox.setDisable(true);


        fadeOut(contextIn, 300);
        fadeOut(contextOut, 300);

        beginReading();
    }

    private void hitBack()
    {
        this.pump.stop();
        this.sourcesBox.setDisable(false);
        this.itemList.getSelectionModel().selectPrevious();
        this.sourcesBox.setDisable(true);

        fadeOut(contextIn, 300);
        fadeOut(contextOut, 300);

        beginReading();
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
            stopOrdered=true;
            pump.stop();
        }
    }

    @FXML
    public void startReadingBtnClicked() {
        activateReadingMode();
    }

    public void filterTextChanged() {
        List<Email> newFilteredEmails = Filters.filterList(filterText.getText(), origEmails);
        itemList.setItems(FXCollections.observableArrayList(newFilteredEmails));
        itemList.getSelectionModel().selectFirst();
    }

    @FXML
    public void handlePlayNextScreenButtons(ActionEvent evt) {
        if (evt.getSource()==btnPlayNextYes) {

            fadeOut(playNextBox, 500);
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(()->playNextBox.setVisible(false));
                }
            }, 500);

            WordRender.clear(currentWordLabel);
            contextIn.setText("");
            contextOut.setText("");

            fadeIn(readerPane, 500);

            this.pump.stop();
            this.sourcesBox.setDisable(false);
            this.itemList.getSelectionModel().selectNext();
            this.sourcesBox.setDisable(true);

            fadeOut(contextIn, 300);
            fadeOut(contextOut, 300);

            beginReading();
        } else
        {
            deactivateReadingMode();
        }
    }

    private void error(String error){
        errorBox.setVisible(true);
        errorBox.setText(String.format("Error: %s", error));
    }

    private void clearError(){
        errorBox.setVisible(false);
        errorBox.setText("");
    }

    public void onErrorBoxClick(){
        clearError();
    }
}
