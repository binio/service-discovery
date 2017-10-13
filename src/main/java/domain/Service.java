package domain;

import java.util.Map;


public class Service {
    private String prefix;
    private String name;
    private String host;
    private String port;
    private String version;
    private String loadFactor;

    public Service(){}
    public Service(Map<String, String> items) {
        for (Map.Entry<String, String> entry : items.entrySet()) {
            if(entry.getKey().equals("prefix")){ this.prefix = entry.getValue(); }
            if(entry.getKey().equals("name")){ this.name = entry.getValue(); }
            if(entry.getKey().equals("host")){ this.host = entry.getValue(); }
            if(entry.getKey().equals("port")){ this.port = entry.getValue(); }
            if(entry.getKey().equals("version")){ this.version = entry.getValue(); }
            if(entry.getKey().equals("loadFactor")){ this.loadFactor = entry.getValue(); }
        }

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
