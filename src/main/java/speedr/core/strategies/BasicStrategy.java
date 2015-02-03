package speedr.core.strategies;

import speedr.core.entities.Word;

/**
 *
 * Implementation of a Strategy for building Word objects with durations. This strategy uses only a
 * frequency chart as a weighting.
 *
 * Strategy pattern is for deciding how to build a Word. The important part here is just the duration.
 * There are many possible algorithms for assigning a duration to a word, and a strategy is an
 * implementation of such an algorithm.
 *
 *
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
