package speedr.core;

import speedr.core.entities.Context;
import speedr.core.strategies.DumbFrequencyStrategy;
import speedr.core.strategies.FrequencyMap;
import speedr.sources.HasContent;
import speedr.core.entities.Sentence;
import speedr.core.entities.Word;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * This is the core class of the Speed Reader family. It represents a stream of Word objects, and has
 * functionality to get a next word, go back a word, etc.
 *
 * A SpeedReaderStream takes an implementation of HasContent as a data source. It will tokenize that
 * data source using the SpeedReadTokenizer + a strategy into a list of Word objects.
 *
 */

public class SpeedReaderStream {

    private final String originalContent;
    private final LinkedList<Sentence> sentences;

    private int currentSentence = 0;
    private int currentWord = 0;

    public SpeedReaderStream(HasContent c, int wpm) {

        originalContent = c.getContent();

        SpeedReadTokenizer srt;

        try {
            srt = new SpeedReadTokenizer(
                new DumbFrequencyStrategy(
                    wpm,
                    FrequencyMap.fromResource("/frequency/frequency_list.json")
                )
            );
        } catch(Exception e){
            throw new RuntimeException(e);
        }

        sentences = srt.parse(originalContent);
    }

    public String getOriginalContent() {
        return originalContent;
    }

    public Word getNextWord() {

        if (currentWord >= sentences.get(currentSentence).getCount()) {
            currentSentence += 1;
            currentWord = 0;
        }

        if (currentSentence >= sentences.size())
            return null;


        Word w = this.sentences.get(currentSentence).getWord(currentWord);
        currentWord += 1;

        return w;
    }

    public void goBackWord() {
        if (currentWord == 0 && currentSentence == 0)
            throw new IllegalArgumentException("can't go back further than the beginning");

        currentWord -= 1;

        if (currentWord < 0) {
            currentSentence -= 1;
            currentWord = this.sentences.get(currentSentence).getCount() - 1;
        }

    }

    public void goBackSentence() {
        if (currentWord == 0 && currentSentence == 0)
            throw new IllegalArgumentException("can't go back further than beginning");

        if (currentWord != 0) {
            currentWord = 0;
        } else {
            currentSentence--;
        }
    }

    public List<Word> getWords() {
        List<Word> words = new ArrayList<>();
        sentences.stream().forEach((s) -> words.addAll(s.getWords()));

        return words;
    }

    public int getBaseTimeToReadMillis(){
        return getWords().stream().mapToInt(Word::getDuration).sum();
    }

    public void rewind()
    {
        currentWord=0;
        currentSentence=0;
    }

    public boolean isAtStart()
    {
        return currentSentence==0 && currentWord == 0;
    }

    public Context getContextWords()
    {
        List<Word> before = new ArrayList<>();
        List<Word> after = new ArrayList<>();

        for (int i=0; i<currentWord-1; i++)
            before.add(sentences.get(currentSentence).getWord(i));

        for (int i=currentWord; i<sentences.get(currentSentence).getCount(); i++)
            after.add(sentences.get(currentSentence).getWord(i));

        return new Context(before, after);
    }


}
