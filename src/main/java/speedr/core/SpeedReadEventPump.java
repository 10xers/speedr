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
 *
 * The Event Pump is a threading/timing utility. It runs through the contents of a SpeedReaderStream and notifies
 * listeners with an event when each word is due to be shown to the user.
 *
 * This class can be plugged into UIs.
 *
 */

public class SpeedReadEventPump {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private List<WordPumpEventListener> wordPumpEventListenerList;
    private int wordsPerMs = 0;
    private final SpeedReaderStream stream;

    private final Object controlLock = new Object();
    private boolean isPaused = false;
    private boolean isEnd = false;

    private Semaphore canRun = new Semaphore(1);

    private Thread counterThread = null;

    public SpeedReadEventPump(SpeedReaderStream stream, int wpm) {
        if (wpm < 0) {
            throw new IllegalArgumentException("cannot use (words per minute) wpm < 0");
        }

        wordsPerMs = wpm / 190;

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

    public boolean isPaused()
    {
        synchronized (controlLock)
        {
            return isPaused;
        }
    }

    public void setPaused(boolean paused) throws InterruptedException {
        synchronized (controlLock)
        {
            if (this.isPaused() ^ paused)
            {
                if (this.isPaused()==false && paused==true)
                {
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

    public void stop()
    {
        synchronized (controlLock)
        {
            logger.debug("stopping ticker");
            this.isEnd = true;
        }
    }

    public boolean isStopped()
    {
        synchronized (controlLock)
        {
            return this.isEnd;
        }
    }


    // start the callback event reading (asynchronously)
    public void start()
    {

        logger.debug("starting ticker");

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Word next;
                while( (next=stream.getNextWord())!=null )
                {
                    logger.debug("processing next word " + next.asText() + " / " + next.getDuration());

                    if (isStopped())
                        break;

                    try {
                        canRun.acquire();
                        logger.debug("acquired pause semaphore");

                        long sleep = wordsPerMs * next.getDuration();
                        logger.debug("sleeping for " + sleep + "ms");
                        System.out.println("sleeping for " + sleep + "ms");
                        Thread.sleep(sleep);

                        if (isStopped())
                            break;

                        logger.debug("firing event for word.");
                        final WordPumpEvent event = new WordPumpEvent(WordPumpEvent.State.IS_MORE, next);

                        Platform.runLater(() -> fireWordPumpEvent(event));

                    } catch (InterruptedException e) {
                        logger.debug("word ticker interrupted in thread", e);
                        throw new IllegalStateException("wait interrupted..", e);
                    } finally {
                        canRun.release();
                        logger.debug("released pause semaphore");
                    }
                }

                Platform.runLater(() -> fireWordPumpEvent(new WordPumpEvent(WordPumpEvent.State.DONE, null))); // notify subscribers we're done here
            }
        });

        t.start();
    }

}
