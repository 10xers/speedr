package speedr.core;

import org.junit.Before;
import org.junit.Test;
import speedr.core.entities.Word;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class SpeedReaderStreamTest {

    private SpeedReaderStream srStream;

    private String[] expectedStream = new String[] { "The", "rain", "in", "spain.", "Is", "something", "or", "other."};

    @Before
    public void configureTest()
    {
        srStream = new SpeedReaderStream(() -> "The rain in spain. Is something or other.");
    }


    @Test
    public void testGetNextWord() throws Exception {

        for (String expectedWord : expectedStream)
        {
            assertEquals(expectedWord, srStream.getNextWord().asText());
        }

        assertNull(srStream.getNextWord());
    }

    @Test
    public void testGoBackWord() throws Exception {

        Arrays.stream(expectedStream).forEach((s)-> { srStream.getNextWord(); }); // bump the counter up

        for (int i=expectedStream.length-1; i>0; i--)
        {
            srStream.goBackWord();
            Word w = srStream.getNextWord();
            srStream.goBackWord();

            assertEquals(expectedStream[i], w.asText());
        }


    }

    @Test
    public void testGoBackSentence() throws Exception {
        Arrays.stream(expectedStream).forEach((s)-> { srStream.getNextWord(); }); // bump the counter up

        srStream.goBackSentence();

        for (int i = 4; i<expectedStream.length; i++)
        {
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
        assertTrue(srStream.getBaseTimeToReadMillis() > 100);
    }
}