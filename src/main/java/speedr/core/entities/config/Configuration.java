package speedr.core.entities.config;

/**
 * Holder object for Configuration data.
 */
public class Configuration {

    private int configVersion = 1;

    private String mailType = "imaps";
    private String user = "speedrorg@gmail.com";
    private String password = "speedrspeedr";
    private String host = "imap.gmail.com";
    private int port = 993;

    private String rootFolder = "INBOX";

    public int getConfigVersion() {
        return configVersion;
    }

    public void setConfigVersion(int configVersion) {
        this.configVersion = configVersion;
    }

    public String getMailType() {
        return mailType;
    }

    public void setMailType(String mailType) {
        this.mailType = mailType;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getRootFolder() {
        return rootFolder;
    }

    public void setRootFolder(String rootFolder) {
        this.rootFolder = rootFolder;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Configuration that = (Configuration) o;

        if (configVersion != that.configVersion) return false;
        if (port != that.port) return false;
        if (host != null ? !host.equals(that.host) : that.host != null) return false;
        if (mailType != null ? !mailType.equals(that.mailType) : that.mailType != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        if (rootFolder != null ? !rootFolder.equals(that.rootFolder) : that.rootFolder != null) return false;
        if (user != null ? !user.equals(that.user) : that.user != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = configVersion;
        result = 31 * result + (mailType != null ? mailType.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (host != null ? host.hashCode() : 0);
        result = 31 * result + port;
        result = 31 * result + (rootFolder != null ? rootFolder.hashCode() : 0);
        return result;
    }

}
