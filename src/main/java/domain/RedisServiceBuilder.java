package domain;

public class RedisServiceBuilder {

    private String prefix;
    private String name;
    private String host;
    private String port;
    private String version;


    public RedisServiceBuilder setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public RedisServiceBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public RedisServiceBuilder setHost(String host) {
        this.host = host;
        return this;
    }

    public RedisServiceBuilder setPort(String port) {
        this.port = port;
        return this;
    }

    public RedisServiceBuilder setVersion(String version) {
        this.version = version;
        return this;
    }

    public String getKey(){
        String delimiter = ":";
        return prefix + delimiter +
                name + delimiter +
                host + delimiter +
                port + delimiter +
                version;
    }

    public String getKeyFromHeartBeat(Service service){
        return this.setHost(service.getHost())
                .setName(service.getName())
                .setPort(service.getPort())
                .setPrefix(service.getPrefix())
                .setVersion(service.getVersion())
                .getKey();
    }

    @Override
    public String toString() {
        return  "prefix='" + prefix + '\'' +
                ", name='" + name + '\'' +
                ", host='" + host + '\'' +
                ", port='" + port + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
