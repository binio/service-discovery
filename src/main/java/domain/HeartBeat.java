package domain;


public class HeartBeat {

    private String name;
    private String host;
    private String prefix;
    private String port;
    private String version;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "HeartBeat{" +
                "name='" + name + '\'' +
                ", host='" + host + '\'' +
                ", prefix='" + prefix + '\'' +
                ", port='" + port + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
