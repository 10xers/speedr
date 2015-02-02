package speedr.core;

import speedr.core.entities.Sentence;
import speedr.core.entities.Word;

import java.util.LinkedList;
import java.util.List;

/**
 * Speedr / Ed
 * 02/02/2015 14:28
 */
public class SpeedReadTokenizer {


    public static LinkedList<Sentence> parse(String s) {
        String whitespaceCleared = s.replaceAll("\\r?\\n", "").replaceAll("\\s+", " ");

        LinkedList<Sentence> sentenceList = new LinkedList<>();

        String[] sentences = whitespaceCleared.split("\\.");
        for (String sentence : sentences) {
            String[] words = sentence.split("\\s");
            List<Word> wordList = new LinkedList<>();

            for (String word : words) {
                if (word.trim().isEmpty() == false)
                    wordList.add(new Word(word, 0));
            }

            Word lastWord = wordList.get(wordList.size() - 1);
            wordList.set(wordList.size() - 1, new Word(lastWord.asText() + ".", lastWord.getDuration()));
            sentenceList.add(new Sentence(wordList));
        }

        return sentenceList;
    }

}
