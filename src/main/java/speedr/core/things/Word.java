package speedr.core.things;

/**
 * Speedr / Ed
 * 02/02/2015 14:12
 */
public class Word {

    private final String word;
    private final int duration;

    public Word(String word, int duration) {
        this.word = word;
        this.duration = duration;
    }


    public String asText() {
        return word;
    }

    public int getDuration() {
        return duration;
    }
}
