package dao;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;
import domain.Service;
import io.lettuce.core.ScanArgs;
import io.lettuce.core.ScanIterator;
import io.lettuce.core.api.sync.RedisCommands;
import org.apache.http.HttpEntity;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;

public class RedisServiceDao implements ServiceDao {

    private RedisConnection connection;
    private static final long LIMIT = 100;
    private static final String KEY_PREFIX = "app";

    public RedisServiceDao(RedisConnection connection) {
        this.connection = connection;
    }

    public List<String> getAllServices(){
        return scanForKeyPattern("*" + KEY_PREFIX + ":*")
                .collect(Collectors.toList());
    }

    public List<String> getServicesByName(String name){
        return scanForKeyPattern("*" + KEY_PREFIX + ":" + name + ":*")
                .collect(Collectors.toList());
    }

    public List<Service> getServiceByNameSorted(String name) {

        return scanForKeyPattern("*" + KEY_PREFIX + ":" + name + ":*")
                .map(this::getKeyObjMapToService).collect(Collectors.toList()).stream()
                .sorted(comparing(Service::getVersion).thenComparing(comparing(Service::getLoadFactor).reversed()).reversed())
                .collect(Collectors.toList());
    }

    public void registerService(Service service, long ttl){
        RedisCommands<String, String> commands = getRedisCommands();
        Map<String, String> values = service.getKeyValues();
        commands.hmset(service.getKey(),values);
        commands.expire(service.getKey(), ttl);
    }

    public HttpResponse<JsonNode> invoke(String domain, String path, String body) {
        /*
        * 1. How to find out type of request PUT / GET /  POST
        * 2. What headers?
        * 3. Are parameters in the body?
        * */
        HttpResponse response = null;
        HttpRequest request = Unirest.post("http://localhost:9005/power/12")
                .header("accept", "application/json").getHttpRequest();
            try{
                response =  request.asJson();
            }catch(UnirestException e){
                e.printStackTrace();
            }
            return response;
    }

    private Service getKeyObjMapToService(String key) {
        RedisCommands<String, String> commands = getRedisCommands();
        Map<String, String> values = commands.hgetall(key);
        return new Service(values);
    }

    private Stream<String> scanForKeyPattern(String match){
        RedisCommands<String, String> commands = getRedisCommands();
        ScanIterator<String> scan = ScanIterator.scan(commands, ScanArgs.Builder.limit(LIMIT).match(match));
        return scan.stream();
    }

    private RedisCommands<String, String> getRedisCommands() {
        return connection.getSyncConnection();
    }
}
