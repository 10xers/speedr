package speedr.core;

import javafx.application.Platform;
import speedr.core.entities.Word;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Speedr / Ed
 * 02/02/2015 16:37
 */
public class SpeedReadEventPump {

    private List<WordPumpEventListener> wordPumpEventListenerList;
    private int wordsPerMs = 0;
    private final SpeedReaderStream stream;

    private final Object pauseLock = new Object();
    private boolean isPaused = false;

    private Timer timer;

    public SpeedReadEventPump(SpeedReaderStream stream, int wpm) {
        if (wpm < 0) {
            throw new IllegalArgumentException("cannot use (words per minute) wpm < 0");
        }

        wordsPerMs = wpm / 60 / 100;

        wordPumpEventListenerList = new ArrayList<>();

        this.stream = stream;

        timer = new Timer();
        timer.schedule(new TimerTask() {
                       @Override
                       public void run() {
                           if (!isPaused()) {
                               Platform.runLater(() -> fireWordPumpEvent(stream.getNextWord()));
                           }
                       }
                   },
                   0,
                   wordsPerMs);

    }

    private void fireWordPumpEvent(final Word w) {
        if (!Platform.isFxApplicationThread())
            throw new IllegalStateException("called from wrong thread!");

        wordPumpEventListenerList.stream().forEach(p -> p.wordPump(w));
    }


    public void addWordPumpEventListener(WordPumpEventListener p) {
        wordPumpEventListenerList.add(p);
    }

    public void removeWordPumpEventListener(WordPumpEventListener p) {
        if (wordPumpEventListenerList.contains(p)) {
            wordPumpEventListenerList.remove(p);
        }
    }

    public boolean isPaused()
    {
        synchronized (pauseLock)
        {
            return isPaused;
        }
    }

    public void setPaused(boolean paused)
    {
        synchronized (pauseLock)
        {
            this.isPaused = paused;
        }
    }

    public void stop()
    {
        setPaused(true);

        synchronized (pauseLock)
        {
            timer.cancel();
        }
    }


}
