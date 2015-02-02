package speedr.core.entities;

import java.util.LinkedList;
import java.util.List;
import java.util.PrimitiveIterator;

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

    public Word getWord(int idx)
    {
        if (idx<0 || idx>words.size()-1)
            throw new IllegalArgumentException("Index does not exist (Subscript out of range)");

        return words.get(idx);
    }

    public int getCount()
    {
        return words.size();
    }

    public List<Word> getWords()
    {
        return this.words;
    }

}
