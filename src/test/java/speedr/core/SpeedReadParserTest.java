package speedr.core;

import org.junit.Test;
import speedr.core.entities.Sentence;
import speedr.core.entities.Word;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class SpeedReadParserTest {

    @Test
    public void testParse() throws Exception {

        String content = "The rain in spain is never the same. And the beer is austere.";

        String[] firstSentenceExpected = new String[]{"The", "rain", "in", "spain", "is", "never", "the", "same."},
                 secondSentenceExpected = new String[]{"And", "the", "beer", "is", "austere." };

        List<Sentence> got = SpeedReadParser.parse(content);

        assertEquals(2, got.size());

        assertEquals(8, got.get(0).getCount());
        assertEquals(5, got.get(1).getCount());


        for (int i=0; i<firstSentenceExpected.length; i++)
        {
            assertEquals(firstSentenceExpected[i],
                         got.get(0).getWord(i).getWord());
        }

        for (int i=0; i<secondSentenceExpected.length; i++)
        {
            assertEquals(secondSentenceExpected[i], got.get(1).getWord(i).getWord());
        }


    }
}