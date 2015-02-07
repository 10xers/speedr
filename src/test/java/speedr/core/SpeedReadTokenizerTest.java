package speedr.core;

import org.junit.Test;
import speedr.core.notthings.FrequencyCoefficientStrategy;
import speedr.core.notthings.FrequencyMap;
import speedr.core.notthings.SpeedReadTokenizer;
import speedr.core.things.Sentence;
import speedr.core.things.Word;

import java.util.List;

import static org.junit.Assert.*;

public class SpeedReadTokenizerTest {

    @Test
    public void testParse() throws Exception {

        String content = "The rain in spain is never the same. And the beer is austere.";

        String[] firstSentenceExpected = new String[]{"The", "rain", "in", "spain", "is", "never", "the", "same."},
                 secondSentenceExpected = new String[]{"And", "the", "beer", "is", "austere." };

        SpeedReadTokenizer srt = null;

        try {
            srt = new SpeedReadTokenizer(
                    new FrequencyCoefficientStrategy(FrequencyMap.fromResource("/frequency/frequency_list.json"), 50)
            );
        } catch(Exception e){
            throw new RuntimeException(e);
        }

        List<Sentence> got = srt.parse(content);

        assertEquals(2, got.size());

        assertEquals(8, got.get(0).getCount());
        assertEquals(5, got.get(1).getCount());

        for (int i=0; i<firstSentenceExpected.length; i++)
        {
            assertEquals(firstSentenceExpected[i],
                         got.get(0).getWord(i).asText());
        }

        for (int i=0; i<secondSentenceExpected.length; i++)
        {
            assertEquals(secondSentenceExpected[i], got.get(1).getWord(i).asText());
        }

    }

    @Test
    public void multilineParsing()
    {
        String multiline = "WAR IS PEACE\n" +
                           "FREEDOM IS SLAVERY\n" +
                           "IGNORANCE IS STRENGTH\n";

        String[] multilineExpected = new String[] { "WAR", "IS", "PEACE",
                                                    "FREEDOM",  "IS", "SLAVERY",
                                                    "IGNORANCE", "IS", "STRENGTH" };


        SpeedReadTokenizer srt = null;

        try {
            srt = new SpeedReadTokenizer(
                    new FrequencyCoefficientStrategy(FrequencyMap.fromResource("/frequency/frequency_list.json"), 50)
            );
        } catch(Exception e){
            throw new RuntimeException(e);
        }

        List<Sentence> sentences = srt.parse(multiline);

        assertEquals(1, sentences.size());
        List<Word> words = sentences.get(0).getWords();

        assertEquals(multilineExpected.length, words.size());

        for (int i=0; i<multilineExpected.length; i++)
        {
            assertEquals(multilineExpected[i], words.get(i).asText());
        }

    }

}