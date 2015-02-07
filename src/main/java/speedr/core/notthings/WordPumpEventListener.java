package speedr.core.notthings;

/**
 *
 * Listener interface for the SpeedReadEventPump. Listeners are notified when they should render each word
 * of the SpeedReaderStream.
 *
 */

public interface WordPumpEventListener {

    public void wordPump(final WordPumpEvent wordPumpEvent);

}
