package speedr.core;

import javafx.application.Platform;
import speedr.core.entities.Word;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

/**
 * Speedr / Ed
 * 02/02/2015 16:37
 */
public class SpeedReadEventPump {

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

        wordsPerMs = wpm / 60 / 100;

        wordPumpEventListenerList = new ArrayList<>();

        this.stream = stream;
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
                    canRun.acquire();
                } else {
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
    public void start(WordPumpEventListener listener)
    {

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Word next;
                while( (next=stream.getNextWord())!=null )
                {
                    if (isStopped())
                        break;

                    try {
                        canRun.acquire();
                        Thread.sleep(wordsPerMs * next.getDuration());

                        if (isStopped())
                            break;

                        Platform.runLater(() -> fireWordPumpEvent(next));

                    } catch (InterruptedException e) {
                        throw new IllegalStateException("wait interrupted..", e);
                    } finally {
                        canRun.release();
                    }
                }
            }
        });

        t.start();
    }

}
