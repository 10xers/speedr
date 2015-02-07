package speedr.core;

import org.junit.* ;
import speedr.core.notthings.FrequencyMap;

import static org.junit.Assert.* ;

public class FrequencyMapTest {

    @Test
    public void testFrequencyMap() throws Exception {

        FrequencyMap fq = FrequencyMap.fromResource("/frequency/frequency_list.json");

        assertTrue(fq != null);
        assertTrue(fq.getMap().containsKey("hello"));
        assertTrue(fq.getMap().get("hello") == 38);

        assertTrue(fq.getCoefficientOf("it") < fq.getCoefficientOf("hello"));

    }

}
