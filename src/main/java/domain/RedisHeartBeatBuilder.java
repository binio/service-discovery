package domain;

public class RedisHeartBeatBuilder {

    private String prefix;
    private String name;
    private String host;
    private String port;
    private String version;


    public RedisHeartBeatBuilder setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public RedisHeartBeatBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public RedisHeartBeatBuilder setHost(String host) {
        this.host = host;
        return this;
    }

    public RedisHeartBeatBuilder setPort(String port) {
        this.port = port;
        return this;
    }

    public RedisHeartBeatBuilder setVersion(String version) {
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

    public String getKeyFromHeartBeat(HeartBeat heartBeat){
        return this.setHost(heartBeat.getHost())
                .setName(heartBeat.getName())
                .setPort(heartBeat.getPort())
                .setPrefix(heartBeat.getPrefix())
                .setVersion(heartBeat.getVersion())
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
