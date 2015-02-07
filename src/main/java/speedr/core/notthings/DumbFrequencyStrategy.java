package speedr.core.notthings;

import speedr.core.things.Word;

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
                duration *= 1.4;
                break;
            }
        }

        if(!frequencyMap.contains(s.toLowerCase())){
            duration *= 1.4;
        } else if(s.length() > 9){
            duration *= 1.4;
        }

        return new Word(s, duration);
    }

}
