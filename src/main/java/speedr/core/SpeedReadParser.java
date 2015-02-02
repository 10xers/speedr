package speedr.core;

import speedr.core.entities.Sentence;
import speedr.core.entities.Word;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Speedr / Ed
 * 02/02/2015 14:28
 */
public class SpeedReadParser {


    public static LinkedList<Sentence> parse(String s)
    {
        String whitespaceCleared = s.replaceAll("\\r?\\n", "").replaceAll("\\s+", "\\s");

        LinkedList<Sentence> sentenceList = new LinkedList<>();

        String[] sentences = whitespaceCleared.split("\\.");
        for (String sentence : sentences)
        {
            String[] words = sentence.split("\\s");
            List<Word> wordList = new LinkedList<>();

            for (String word : words)
            {
                wordList.add(new Word(word, 0));
            }

            sentenceList.add(new Sentence(wordList));
        }

        return sentenceList;
    }

}
