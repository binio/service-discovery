package domain;

import java.util.Map;


public class Service {
    public  String prefix;
    private String name;
    private String host;
    private String port;
    private String version;
    private String loadFactor;

    public Service(Map<String, String> items) {
        this.prefix = items.get("prefix");
        this.name = items.get("name");
        this.host = items.get("host");
        this.port = items.get("port");
        this.version = items.get("version");
        this.loadFactor = items.get("loadFactor");

    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

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

    public String getLoadFactor() {
        return loadFactor;
    }

    public void setLoadFactor(String loadFactor) {
        this.loadFactor = loadFactor;
    }

}
