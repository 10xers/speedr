package speedr.core.strategies;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * Loads the word frequency map. Can be interrogated for speed coefficients.
 */

public class FrequencyMap {

    private Map<String, Integer> frequencyMap;
    private int highestRank = 1;

    private FrequencyMap(Map<String, Integer> map) {

        this.frequencyMap = map;

        // Pre-calculate the highest value for future normalisation

        for (int i : map.values()) {
            if (i > this.highestRank) {
                this.highestRank = i;
            }
        }

    }

    public Map<String, Integer> getMap() {
        return this.frequencyMap;
    }

    public float getCoefficientOf(String word) {

        float rank = this.frequencyMap.getOrDefault(word, 0);
        float normal = rank / highestRank;

        if (rank == 0) {
            // not in map. Difficult word
            return 2.0f;
        } else if (normal > 0.1f) {
            // very common word
            return 1.0f;
        } else if (normal > 0.01f) {
            // moderately common word
            return 1.3f;
        } else {
            // somewhat common word
            return 1.5f;
        }

    }

    public static FrequencyMap fromResource(String resource) throws FileNotFoundException {

        // unmarshall from json

        Map<String, Integer> map;

        try {
            ObjectMapper mapper = new ObjectMapper();
            map = mapper.readValue(
                    new File(FrequencyMap.class.getResource(resource).toURI()),
                    new TypeReference<HashMap<String, Integer>>() {
                    }
            );
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }

        return new FrequencyMap(map);

    }

}
