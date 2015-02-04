package speedr.core;

import speedr.core.entities.Sentence;
import speedr.core.entities.Word;
import speedr.core.strategies.Strategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * The tokenizer is a utility class that splits a source into Word objects. It takes a strategy.
 *
 */

public class SpeedReadTokenizer {

    private Strategy strategy;
    private static String[] punctuation = {".", ",", "?", "!", "\"", "'", ";", ":"};
    public SpeedReadTokenizer(Strategy s){
        this.strategy = s;
    }

    private List<String> stringToSentences(String s) {

        List<String> sentences = new ArrayList<>(10);

        String sentence = "";

        for(String word : s.split("\\s")) {

            sentence += word + " ";

            if(Arrays.stream(punctuation).anyMatch(mark -> word.endsWith(mark))){
                sentences.add(sentence);
                sentence = "";
            }

        }

        if(! sentence.trim().isEmpty()){
            sentences.add(sentence);
        }

        return sentences;

    }

    public LinkedList<Sentence> parse(String s) {

        String whitespaceCleared = s.replaceAll("\\r?\\n", " ").replaceAll("\\s+", " ");

        LinkedList<Sentence> sentenceList = new LinkedList<>();

        for (String sentence : stringToSentences(whitespaceCleared)) {

            String[] words = sentence.split("\\s");
            List<Word> wordList = new LinkedList<>();

            for (String word : words) {
                if (word.trim().isEmpty() == false)
                    wordList.add(this.strategy.wordFor(word));
            }

            if (!wordList.isEmpty()) {
                Word lastWord = wordList.get(wordList.size() - 1);
                wordList.set(wordList.size() - 1, new Word(lastWord.asText(), lastWord.getDuration()));
                sentenceList.add(new Sentence(wordList));
            }
        }

        return sentenceList;
    }

}
