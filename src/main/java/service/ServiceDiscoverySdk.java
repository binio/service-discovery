package service;

import dao.RedisConnection;
import domain.HeartBeat;
import domain.RedisHeartBeatBuilder;
import domain.Service;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

import java.util.*;


public class ServiceDiscoverySdk {

    private RedisConnection connection;

    public ServiceDiscoverySdk(RedisConnection connection){
        this.connection = connection;
    }

    public List getAllServices() {
        RedisCommands<String, String> commands = getRedisCommands();
        List keys = commands.keys("*app:*");
        keys.stream().sorted().forEach(System.out::println);
        return keys;
    }

    public void registerHeartbeat(HeartBeat heartBeat, long ttl) {
        RedisCommands<String, String> commands = getRedisCommands();
        String key = setKeyFromHeartBeat(heartBeat);
        Map values = setKeyValues(heartBeat);
        commands.hmset(key,values);
        commands.expire(key, ttl);
    }


    private RedisCommands<String, String> getRedisCommands() {
        StatefulRedisConnection<String, String> connection = this.connection.getConnection();
        return connection.sync();
    }

    public String setKeyFromHeartBeat(HeartBeat heartBeat){
        RedisHeartBeatBuilder redisHeartBeatBuilder = new RedisHeartBeatBuilder();
        return redisHeartBeatBuilder.getKeyFromHeartBeat(heartBeat);
    }

    public Map<String, String> setKeyValues(HeartBeat heartBeat) {
        HashMap values = new HashMap();
        values.put("prefix", heartBeat.getPrefix());
        values.put("name", heartBeat.getName());
        values.put("host", heartBeat.getHost());
        values.put("port", heartBeat.getPort());
        values.put("version", heartBeat.getVersion());
        return values;
    }

    public List getServiceByName(String name) {
        RedisCommands<String, String> commands = getRedisCommands();
        List keys = commands.keys("*app:" + name + ":*");
        return keys;
    }

    public List<Service> getServiceByNameSorted(String name) {
        RedisCommands<String, String> commands = getRedisCommands();
        List keys = commands.keys("*app:" + name + ":*");
        List<Service> services = getKeyValues(keys);
        List<Service> sortedKeys = sortServices(services);
        return sortedKeys;
    }

    private List<Service> getKeyValues(List<String> keys) {
        List<Service> services = new ArrayList<>();
        for(String key : keys){
            Service service = getKeyObjMapToService(key);
            services.add(service);
        }
        return services;
    }

    private Service getKeyObjMapToService(String key) {
        RedisCommands<String, String> commands = getRedisCommands();
        Map<String, String> values = commands.hgetall(key);
        Service service = new Service(values);
        return service;
    }

    private List<Service> sortServices(List<Service> services) {
         services.sort(Comparator.comparing(Service::getVersion));
         Collections.reverse(services);
         return services;
    }
}
