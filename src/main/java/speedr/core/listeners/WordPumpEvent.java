package speedr.core.listeners;

import speedr.core.entities.Word;

/**
 * Speedr / Ed
 * 03/02/2015 13:30
 */
public class WordPumpEvent {

    public enum State {
        DONE,
        IS_MORE
    }

    private final State s;
    private final Word w;

    public WordPumpEvent(State s, Word w) {
        this.w = w;
        this.s = s;
    }

    public final State getState() {
        return s;
    }

    public final Word getWord() {
        return w;
    }

    public final boolean isDone()
    {
        return getState().equals(State.DONE);
    }

}
