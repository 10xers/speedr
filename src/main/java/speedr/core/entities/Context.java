package speedr.core.entities;

import java.util.List;

/**
 * Speedr / Ed
 * 05/02/2015 00:27
 */
public class Context {

    private List<Word> before;
    private List<Word> after;

    public Context(List<Word> before, List<Word> after) {
        this.before = before;
        this.after = after;
    }

    public List<Word> getAfter() {
        return after;
    }

    public List<Word> getBefore() {
        return before;
    }
}
