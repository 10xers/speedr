package speedr.core.notthings;

import speedr.core.things.Word;

/**
 *
 * This strategy makes every word duration the same, unless it ends with a punctuation mark.
 *
 */

public class ConstantStrategy implements Strategy {

    private static String[] punctuation = {".", ",", "?", "!", "\"", "'", ";", ":"};

    private int standardDuration = 50;

    public ConstantStrategy(int wpm){
        this.standardDuration = 60000 / wpm;
    }

    @Override
    public Word wordFor(String s) {

        int duration = standardDuration;

        for(String p : punctuation){
            if(s.endsWith(p)){
                duration *= 1.65;
                break;
            }
        }

        return new Word(s, duration);
    }

}
