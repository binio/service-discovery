package dao;


import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;

public class RedisConnection {

    private StatefulRedisConnection<String, String> connection;
    private RedisClient client;

    public RedisConnection(String host, Integer port, String password, Integer db) {
        RedisURI redisUri = RedisURI.Builder.redis(host)
                .withPort(port)
                .withPassword(password)
                .withDatabase(db)
                .build();
        client = RedisClient.create(redisUri);
        connection = client.connect();

    }

    public StatefulRedisConnection<String, String> getConnection() {
        return connection;
    }


}