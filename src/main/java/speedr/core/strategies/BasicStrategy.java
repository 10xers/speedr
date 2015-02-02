package speedr.core.strategies;

import speedr.core.entities.FrequencyMap;
import speedr.core.entities.Word;

/**
 * Created by rhizome on 02/02/2015.
 */
public class BasicStrategy implements Strategy {

    private FrequencyMap frequencies;
    private int baseDurationMilliseconds;

    public BasicStrategy(FrequencyMap fm, int baseDurationMilliseconds) {
        this.frequencies = fm;
        this.baseDurationMilliseconds = baseDurationMilliseconds;
    }

    @Override
    public Word wordFor(String s) {

        float coefficient = frequencies.getCoefficientOf(s);
        return new Word(s, (int)(this.baseDurationMilliseconds * coefficient));
    }

}
