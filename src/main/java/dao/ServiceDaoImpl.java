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
import java.util.stream.Collectors;

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
        Map<String, String> values = service.getKeyValues();
        commands.hmset(service.getKey(),values);
        commands.expire(service.getKey(), ttl);
    }

    private RedisCommands<String, String> getRedisCommands() {
        StatefulRedisConnection<String, String> connection = this.connection.getConnection();
        return connection.sync();
    }

    private List<Service> getKeyValues(List<String> keys) {
        return keys.stream().map(this::getKeyObjMapToService).collect(Collectors.toList());
    }

    private Service getKeyObjMapToService(String key) {
        RedisCommands<String, String> commands = getRedisCommands();
        Map<String, String> values = commands.hgetall(key);
        return new Service(values);
    }

    private List<String> scanForKeyPattern(String match){
        RedisCommands<String, String> commands = getRedisCommands();
        ScanIterator<String> scan = ScanIterator.scan(commands, ScanArgs.Builder.limit(LIMIT).match(match));
        return scan.stream().collect(Collectors.toList());
    }

}
