package dao;

import domain.Service;
import io.lettuce.core.ScanArgs;
import io.lettuce.core.ScanIterator;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import util.ServiceUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ServiceDaoImpl implements ServiceDao {

    private RedisConnection connection;
    private static final long LIMIT = 100;
    private static final String KEY_PREFIX = "app";

    public ServiceDaoImpl(RedisConnection connection) {
        this.connection = connection;
    }

    public List<String> getAllServices(){
        return scanForKeyPattern("*" + KEY_PREFIX + ":*");
    }

    public List<String> getServicesByName(String name){
        return scanForKeyPattern("*" + KEY_PREFIX + ":" + name + ":*");
    }

    public List<Service> getServiceByNameSorted(String name) {
        List<String> keys = scanForKeyPattern("*" + KEY_PREFIX + ":" + name + ":*");
        List<Service> services = getKeyValues(keys);
        return ServiceUtils.sortServices(services);
    }

    public void registerService(Service service, long ttl){
        RedisCommands<String, String> commands = getRedisCommands();
        String key = ServiceUtils.setKeyFromHeartBeat(service);
        Map<String, String> values = ServiceUtils.setKeyValues(service);
        commands.hmset(key,values);
        commands.expire(key, ttl);
    }

    private RedisCommands<String, String> getRedisCommands() {
        StatefulRedisConnection<String, String> connection = this.connection.getConnection();
        return connection.sync();
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
        return new Service(values);
    }

    private List<String> scanForKeyPattern(String match){
        RedisCommands<String, String> commands = getRedisCommands();
        ScanIterator<String> scan = ScanIterator.scan(commands, ScanArgs.Builder.limit(LIMIT).match(match));

        List<String> keys = new ArrayList<>();
        while (scan.hasNext()) {
            keys.add(scan.next());
        }
        return keys;
    }

}
