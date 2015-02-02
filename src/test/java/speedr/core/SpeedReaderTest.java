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

        while(true) {

            Word word = s.getNextWord();

            if(word != null) {
                out.add(word.asText());
            } else {
                break;
            }

        }

        assertTrue(out.get(0).equals("this"));

    }

    private class MockContent implements HasContent {

        @Override
        public String getContent() {
            return "this is a sentence";
        }
    }

}
