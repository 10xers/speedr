package speedr.core.strategies;

import speedr.core.entities.Word;

/**
 * Created by rhizome on 02/02/2015.
 */
public class BasicStrategy implements Strategy {

    @Override
    public Word wordFor(String s) {
        return new Word(s, 0);
    }

}
