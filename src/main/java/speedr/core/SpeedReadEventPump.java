package speedr.core;

import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import speedr.core.entities.Word;
import speedr.core.listeners.WordPumpEvent;
import speedr.core.listeners.WordPumpEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * The Event Pump is a threading/timing utility. It runs through the contents of a SpeedReaderStream and notifies
 * listeners with an event when each word is due to be shown to the user.
 * <p>
 * This class can be plugged into UIs.
 */

public class SpeedReadEventPump {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private List<WordPumpEventListener> wordPumpEventListenerList;
    private final SpeedReaderStream stream;

    private final Object controlLock = new Object();
    private boolean isPaused = false;
    private boolean isEnd = false;

    private Semaphore canRun = new Semaphore(1);

    private Thread counterThread = null;

    public SpeedReadEventPump(SpeedReaderStream stream) {
        if (stream == null)
            throw new IllegalArgumentException("stream cannot be null");

        wordPumpEventListenerList = new ArrayList<>();
        this.stream = stream;
    }

    private void fireWordPumpEvent(final WordPumpEvent wpe) {
        if (!Platform.isFxApplicationThread())
            throw new IllegalStateException("called from wrong thread!");


        wordPumpEventListenerList.stream().forEach(p -> p.wordPump(wpe));
    }


    public void addWordPumpEventListener(WordPumpEventListener p) {
        wordPumpEventListenerList.add(p);
    }

    public void removeWordPumpEventListener(WordPumpEventListener p) {
        if (wordPumpEventListenerList.contains(p)) {
            wordPumpEventListenerList.remove(p);
        }
    }

    public boolean isPaused() {
        synchronized (controlLock) {
            return isPaused;
        }
    }

    public void setPaused(boolean paused) throws InterruptedException {
        synchronized (controlLock) {
            if (this.isPaused() ^ paused) {
                if (this.isPaused() == false && paused == true) {
                    logger.debug("pausing ticker");
                    canRun.acquire();
                } else {
                    logger.debug("unpausing ticker");
                    canRun.release();
                }

                this.isPaused = paused;
            }
        }
    }

    public void stop() {
        synchronized (controlLock) {
            logger.debug("stopping ticker");
            this.isEnd = true;
        }
    }

    public boolean isStopped() {
        synchronized (controlLock) {
            return this.isEnd;
        }
    }


    // start the callback event reading (asynchronously)
    public void start() {

        logger.debug("starting ticker");

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                Word next = null;

                try {
                    do {
                        canRun.acquire();
                        logger.debug("acquired pause semaphore");

                        if (isStopped())
                            break;

                        next = stream.getNextWord();

                        if (next!=null) {

                            logger.debug("processing next word " + next + " / " + next.getDuration());

                            if (isStopped())
                                break;

                            logger.debug("firing event for word.");
                            final WordPumpEvent event = new WordPumpEvent(WordPumpEvent.State.IS_MORE, next);

                            Platform.runLater(() -> fireWordPumpEvent(event));

                            canRun.release();

                            Thread.sleep(next.getDuration());
                        }

                    } while (next != null);
                } catch (InterruptedException e) {

                    logger.debug("word ticker interrupted in thread", e);
                    throw new IllegalStateException("wait interrupted..", e);

                } finally {
                    canRun.release();
                    logger.debug("released pause semaphore");
                }

                final Word lastWord = next == null ? null : next;
                Platform.runLater(() -> fireWordPumpEvent(new WordPumpEvent(WordPumpEvent.State.DONE, lastWord)));
            }


        });

        t.start();
    }

    public boolean isAtStart()
    {
        return stream.isAtStart();
    }

    public void goBackWordAndReFire() throws InterruptedException {

        if (!Platform.isFxApplicationThread())
            throw new IllegalStateException("wrong caller");

        if (this.isPaused() == false)
            this.setPaused(true);


        this.stream.goBackWord();
        fireWordPumpEvent(new WordPumpEvent(WordPumpEvent.State.IS_MORE, this.stream.getNextWord()));
        this.stream.goBackWord();
    }

    public void goBackSentenceAndRefire() throws InterruptedException {

        if (!Platform.isFxApplicationThread())
            throw new IllegalStateException("nope");

        if (this.isPaused())
            this.setPaused(true);

        this.stream.goBackSentence();
        fireWordPumpEvent(new WordPumpEvent(WordPumpEvent.State.IS_MORE, this.stream.getNextWord()));
        this.stream.goBackWord();
    }
}
