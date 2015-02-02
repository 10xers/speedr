package speedr.core.entities;

import java.util.LinkedList;
import java.util.List;

/**
 * Speedr / Ed
 * 02/02/2015 14:12
 */
public class Sentence {

    private LinkedList<Word> words;

    public Sentence(List<Word> words)
    {
        this.words = new LinkedList<>();
        this.words.addAll(words);
    }


}
