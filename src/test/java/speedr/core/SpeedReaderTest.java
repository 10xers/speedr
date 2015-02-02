package speedr.core;

import org.junit.* ;
import speedr.core.entities.FrequencyMap;
import speedr.core.entities.HasContent;
import speedr.core.entities.Word;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.* ;

public class SpeedReaderTest {

    @Test
    public void testSystem() {

        SpeedReaderStream s = new SpeedReaderStream(new MockContent());

        List<String> out = new ArrayList<>(5);

        Word w;

        while((w = s.getNextWord()) != null) {
            out.add(w.asText());
        }

        assertTrue(out.get(0).equals("this"));
        assertTrue(s.getWords().get(0).asText().equals(out.get(0)));

        s.goBackWord();
        assertTrue(s.getNextWord().asText().equals("sentence."));

    }

    private class MockContent implements HasContent {

        @Override
        public String getContent() {
            return "this is a sentence. Another sentence.";
        }
    }

}
