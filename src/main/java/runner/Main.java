package runner;

import dao.RedisConnection;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import redis.embedded.RedisServer;
import service.ServiceDiscoverySdk;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class Main {
    public static void main(String[] args) {
//        Configurations configs = new Configurations();
//
//            try {
//                Configuration config = configs.properties(new File("redis.properties"));
//                System.out.println(config.getString("redis.host"));
//                System.out.println(config.getString("redis.port"));
//            } catch (ConfigurationException e) {
//                e.printStackTrace();
//            }

        RedisConnection connection = new RedisConnection("localhost", 6379, "",0);
        ServiceDiscoverySdk service = new ServiceDiscoverySdk(connection);
        List<String> services = service.getAllServices();


            RedisServer server = RedisServer.builder()
                    .port(6377)
                    //.redisExecProvider(customRedisExec) //com.github.kstyrc (not com.orange.redis-embedded)
                    .setting("maxmemory 128M") //maxheap 128M
                    .build();
            server.start();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (server != null) {
            server.stop();
            System.out.println("Stop server");
        }


    }
}
