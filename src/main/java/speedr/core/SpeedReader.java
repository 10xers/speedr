package speedr.core;

import speedr.core.entities.HasContent;
import speedr.core.entities.Sentence;

import java.util.LinkedList;

/**
 * Speedr / Ed
 * 02/02/2015 14:10
 */
public class SpeedReader {

    private final String originalContent;
    private final LinkedList<Sentence> sentences;

    private int currentSentence = 0;
    private int currentWord = 0;

    public SpeedReader(HasContent c) {
        originalContent = c.getContent();
        sentences = SpeedReadParser.parse(originalContent);
    }

    public String getOriginalContent() {
        return originalContent;
    }

    public void getNextWord() {

    }

    public void goBackWord() {

    }

    public void goBackSentence()
    {

    }


}
