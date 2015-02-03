package speedr.core.strategies;

import speedr.core.entities.Word;

/**
 *
 * This strategy makes every word duration the same, unless it ends with a punctuation mark.
 *
 */

public class ConstantStrategy implements Strategy {

    private static String[] punctuation = {".", ",", "?", "!", "\"", "'", ";", ":"};

    @Override
    public Word wordFor(String s) {

        int duration = 50;

        for(String p : punctuation){
            if(s.endsWith(p)){
                duration = 90;
            }
        }

        return new Word(s, duration);
    }

}
