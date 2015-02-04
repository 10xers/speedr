package speedr.core;

import org.junit.* ;
import speedr.core.entities.Word;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.* ;

public class SpeedReaderTest {

    @Test
    public void testSystem() {

        SpeedReaderStream s = new SpeedReaderStream(() -> "this is a sentence. Another sentence.", 500);

        List<String> out = new ArrayList<>(5);

        Word w;

        while((w = s.getNextWord()) != null) {
            out.add(w.asText());
        }

        assertTrue(out.get(0).equals("this"));
        assertTrue(out.get(3).equals("sentence."));
        assertTrue(s.getWords().get(0).asText().equals(out.get(0)));

        s.goBackWord();
        assertTrue(s.getNextWord().asText().equals("sentence."));

    }

}
