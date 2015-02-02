package speedr.core.entities;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import java.io.*;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class FrequencyMap {

    private Map<String, Integer> frequencyMap;

    private FrequencyMap(Map<String, Integer> map){
        this.frequencyMap = map;
    }

    public Map<String, Integer> getMap(){
        return this.frequencyMap;
    }

    public static FrequencyMap fromFile() throws FileNotFoundException {

        // unmarshall from json

        Map<String,Integer> map;

        try {
            ObjectMapper mapper = new ObjectMapper();
            map = mapper.readValue(
                new File(FrequencyMap.class.getResource("/frequency/frequency_list.json").toURI()),
                new TypeReference<HashMap<String,Integer>>(){}
            );
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }

        return new FrequencyMap(map);

    }

}
