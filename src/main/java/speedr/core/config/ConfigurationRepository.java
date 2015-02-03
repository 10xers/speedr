package speedr.core.config;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import speedr.core.entities.config.Configuration;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Speedr / Ed
 * 03/02/2015 15:39
 */
public class ConfigurationRepository {

    private static int[] supportedVersions = new int[] { 1 };

    private static final Logger l = LoggerFactory.getLogger(ConfigurationRepository.class);

    private static final String defaultFilename = "speedr.conf";
    private static final String defaultPath = System.getProperty("user.home") + "." + File.pathSeparator + "speedr/";

    public static Configuration load() throws IOException, CorruptedConfigException {
        return loadFromFile(new File(defaultPath + defaultFilename));
    }

    public static Configuration loadFromFile(File f) throws IOException, CorruptedConfigException {
        if (f==null)
            throw new IllegalArgumentException("cannot load from null config file");

        if (!f.canRead())
        {
            l.warn("configuration file {} is not readable, loading default config instead", f.getAbsoluteFile());
            return new Configuration(); // default config
        }

        ObjectMapper objectMapper = new ObjectMapper();
        Configuration loaded = objectMapper.readValue(f, Configuration.class);

        boolean supportedConfigVersion = Arrays.asList(supportedVersions).contains(loaded.getConfigVersion());

        if ( !supportedConfigVersion )
            throw new CorruptedConfigException("unsupported config version " + loaded.getConfigVersion());

        return loaded;
    }

    public static void save(Configuration configuration) throws IOException {
        save(configuration, new File(defaultPath+defaultFilename));
    }

    public static void save(Configuration configuration, File to) throws IOException {
        if(to==null)
            throw new IllegalArgumentException("can't write config to null file");

        if(to.exists())
            l.warn("overwriting existing config file {}", to.getAbsoluteFile());

        if(!to.canWrite())
            throw new IllegalArgumentException("insufficient perms to write to " + to.getAbsolutePath());


        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(to, configuration);
    }

}
