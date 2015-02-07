package speedr.core.config;

import org.junit.AfterClass;
import org.junit.Test;
import speedr.core.notthings.ConfigurationRepository;
import speedr.core.notthings.CorruptedConfigException;
import speedr.core.things.Configuration;

import java.io.File;

import static org.junit.Assert.*;

public class ConfigurationRepositoryTest {

    private static final String testfilewithpath = "speedr_test.conf";

    @Test
    public void testLoad() throws Exception {
        Configuration set = new Configuration();
        Configuration response = null;

        ConfigurationRepository.save(set, new File(testfilewithpath));
        response = ConfigurationRepository.loadFromFile(new File(testfilewithpath));

        assertEquals(response, set);
    }

    @Test(expected=CorruptedConfigException.class)
    public void testLoadFromInvalidFile() throws Exception {

        Configuration broken = new Configuration() {
            @Override
            public int getConfigVersion() {
                return 123123123;
            }
        };

        ConfigurationRepository.save(broken, new File(testfilewithpath));
        ConfigurationRepository.loadFromFile(new File(testfilewithpath));
    }

    @AfterClass
    public static void cleanup()
    {
        new File(testfilewithpath).delete();
    }
}