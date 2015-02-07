package speedr.core;

import org.junit.Before;
import org.junit.Test;
import speedr.core.notthings.Context;
import speedr.core.notthings.SpeedReaderStream;
import speedr.core.things.Word;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class SpeedReaderStreamTest {

    private SpeedReaderStream srStream;

    private static final String[] expectedStream = new String[]{"The", "rain", "in", "spain.", "Is", "something", "or", "other."};

    private static final String content = "The rain in spain. Is something or other.";

    @Before
    public void configureTest() {
        srStream = new SpeedReaderStream(() -> content, 500);
    }


    @Test
    public void testGetNextWord() throws Exception {

        for (String expectedWord : expectedStream) {
            assertEquals(expectedWord, srStream.getNextWord().asText());
        }

        assertNull(srStream.getNextWord());
    }

    @Test
    public void testGoBackWord() throws Exception {

        Arrays.stream(expectedStream).forEach((s) -> {
            srStream.getNextWord();
        }); // bump the counter up

        for (int i = expectedStream.length - 1; i > 0; i--) {
            srStream.goBackWord();
            Word w = srStream.getNextWord();
            srStream.goBackWord();

            assertEquals(expectedStream[i], w.asText());
        }


    }

    @Test
    public void testGoBackSentence() throws Exception {
        Arrays.stream(expectedStream).forEach((s) -> {
            srStream.getNextWord();
        }); // bump the counter up

        srStream.goBackSentence();

        for (int i = 4; i < expectedStream.length; i++) {
            assertEquals(expectedStream[i], srStream.getNextWord().asText());
        }

        srStream.goBackSentence();
        srStream.getNextWord();
        srStream.goBackSentence();
        assertEquals("Is", srStream.getNextWord().asText());
        srStream.goBackSentence();
        srStream.goBackSentence();
        assertEquals("The", srStream.getNextWord().asText());
    }

    @Test
    public void testGetWords() throws Exception {

        List<Word> words = srStream.getWords();

        assertEquals(expectedStream.length, words.size());

    }

    @Test
    public void testTimeToRead() {
        int calculated = 0;
        for (Word c : srStream.getWords())
            calculated += c.getDuration();

        assertEquals(calculated, srStream.getBaseTimeToReadMillis());
    }

    @Test
    public void testGetOriginalContent() throws Exception {
        assertEquals(content, srStream.getOriginalContent());
    }

    @Test
    public void testRewind() throws Exception {
        srStream.getNextWord();
        srStream.getNextWord();
        srStream.rewind();
        assertEquals("The", srStream.getNextWord().asText());
    }

    @Test
    public void testIsAtStart() throws Exception {

        assertTrue(srStream.isAtStart());

        srStream.getNextWord();
        srStream.getNextWord();
        srStream.rewind();

        assertTrue(srStream.isAtStart());
    }

    @Test
    public void testGetContextWords() throws Exception {
        srStream.getNextWord();
        srStream.getNextWord();

        Context c = srStream.getContextWords();

        // "The rain in spain

        assertEquals(1, c.getBefore().size());
        assertEquals(2, c.getAfter().size());

        assertEquals("The", c.getBefore().get(0).asText());
        assertEquals("in", c.getAfter().get(0).asText());
        assertEquals("spain.", c.getAfter().get(1).asText());
    }
}