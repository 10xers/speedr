package speedr.core;

import speedr.core.entities.HasContent;
import speedr.core.entities.Sentence;
import speedr.core.entities.Word;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Speedr / Ed
 * 02/02/2015 14:10
 */
public class SpeedReaderStream {

    private final String originalContent;
    private final LinkedList<Sentence> sentences;

    private int currentSentence = 0;
    private int currentWord = 0;

    public SpeedReaderStream(HasContent c) {
        originalContent = c.getContent();
        sentences = SpeedReadTokenizer.parse(originalContent);
    }

    public String getOriginalContent() {
        return originalContent;
    }

    public Word getNextWord() {

        if ( currentWord >= sentences.get(currentSentence).getCount() )
        {
            currentSentence+=1;
            currentWord=0;
        }

        if (currentSentence>=sentences.size())
            return null;


        Word w = this.sentences.get(currentSentence).getWord(currentWord);
        currentWord+=1;

        return w;
    }

    public void goBackWord() {
        if (currentWord==0&&currentSentence==0)
            throw new IllegalArgumentException("can't go back further than the beginning");

        currentWord -= 1;

        if (currentWord<0)
        {
            currentSentence-=1;
            currentWord = this.sentences.get(currentSentence).getCount()-1;
        }

    }

    public void goBackSentence()
    {
        if (currentWord==0 && currentSentence==0)
            throw new IllegalArgumentException("can't go back further than beginning");

        if (currentWord!=0)
        {
            currentWord = 0;
        } else {
            currentSentence--;
        }
    }

    public List<Word> getWords()
    {
        List<Word> words = new ArrayList<>();
        sentences.stream().forEach( (s) -> words.addAll(s.getWords()) );

        return words;
    }



}
