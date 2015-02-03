package speedr.core.config;

/**
 * Speedr / Ed
 * 03/02/2015 16:05
 */
public class CorruptedConfigException extends Exception {

    public CorruptedConfigException(String msg)
    {
        super(msg);
    }

    public CorruptedConfigException(String msg, Exception e)
    {
        super(msg, e);
    }

}
