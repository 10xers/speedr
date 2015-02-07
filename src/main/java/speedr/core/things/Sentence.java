package speedr.core.things;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * A Sentence represents a series of Word objects.
 *
 * This was implemented to allow us to easily go back or forward by whole
 * sentences, as well as individual words. In the future there should also
 * be a slight pause after a sentence has finished.
 *
 */

public class Sentence {

    private LinkedList<Word> words;

    public Sentence(List<Word> words) {
        this.words = new LinkedList<>();
        this.words.addAll(words);
    }

    public Word getWord(int idx) {
        if (idx < 0 || idx > words.size() - 1)
            throw new IllegalArgumentException("Index does not exist (Subscript out of range)");

        return words.get(idx);
    }

    public int getCount() {
        return words.size();
    }

    public List<Word> getWords() {
        return this.words;
    }

}
