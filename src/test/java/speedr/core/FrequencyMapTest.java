package speedr.core;

import org.junit.* ;
import speedr.core.entities.FrequencyMap;

import static org.junit.Assert.* ;

public class FrequencyMapTest {

    @Test
    public void testFrequencyMap() throws Exception {

        FrequencyMap fq = FrequencyMap.fromFile();

        assertTrue(fq != null);
        assertTrue(fq.getMap().containsKey("hello"));
        assertTrue(fq.getMap().get("hello") == 38);

        assertTrue(fq.getCoefficientOf("it") < fq.getCoefficientOf("hello"));

    }

}
