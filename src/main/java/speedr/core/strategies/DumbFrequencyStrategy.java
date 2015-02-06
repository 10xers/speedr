package speedr.core.strategies;

import speedr.core.entities.Word;

/**
 * This strategy uses a constant value, a punctuation modifier, and a complexity
 * modifier. It combines the FrequencyCoefficientStrategy and ConstantStrategy.
 */

public class DumbFrequencyStrategy implements Strategy {

    private static String[] punctuation = {".", ",", "?", "!", "\"", "'", ";", ":"};

    private int standardDuration = 50;
    private final FrequencyMap frequencyMap;

    public DumbFrequencyStrategy(int wpm, FrequencyMap fm){
        this.standardDuration = 60000 / wpm;
        this.frequencyMap = fm;
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

        if(!frequencyMap.contains(s.toLowerCase())){
            duration *= 1.65;
        }

        return new Word(s, duration);
    }

}
