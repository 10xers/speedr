package speedr.core.notthings;

import speedr.core.things.Word;

/**
 *
 * Strategy pattern for deciding how to build a Word. The important part here is just the duration.
 * There are many possible algorithms for assigning a duration to a word, and a strategy is an
 * implementation of such an algorithm.
 *
 * The Strategy is used by the tokenizer to build Word objects.
 *
 */

public interface Strategy {

    public Word wordFor(String s);

}
