package service;

import dao.RedisConnection;
import domain.Service;
import io.lettuce.core.ScanArgs;
import io.lettuce.core.ScanIterator;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import redis.embedded.RedisServer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;


@RunWith(MockitoJUnitRunner.class)
public class ServiceDiscoverySdkTest {

    private RedisServer redisServer;
    private RedisConnection connection;
    private ServiceDiscoverySdk service;

    private static final String REDIS_TEST_HOST = "localhost";
    private static final String REDIS_MEMORY_HEAP = "maxmemory 128M";
    private static final Integer REDIS_TEST_PORT = 6370;

    @Before
    public void setUp() {
        //setupServerAndStart();
        prepareService();
        addDummyData();
    }

    @After
    public void tearDown() {
        flushTestData();
        //redisServer.stop();
    }


    private void prepareService() {
        connection = new RedisConnection(REDIS_TEST_HOST, REDIS_TEST_PORT, "",0);
        service = new ServiceDiscoverySdk(connection);
    }

    @Test
    public void getAllServices() throws Exception {
        assertEquals(6, service.getAllServices().size());
    }

    @Test
    public void getServiceByName() {
        //given
        //service name 'auth' set with addDummyData()

        //when
        List keys = service.getServiceByName("auth");

        //then
        assertEquals(5, keys.size());
    }

    @Test
    public void getServiceByNameSorted() {
        //given
        //service name 'auth' set with addDummyData() has 5 instances in Redis

        //when
        List<Service> keys = service.getServiceByNameSorted("auth");

        //then
        //services in collection need to be sorted by 'version'

        Service serviceOne = keys.get(0);
        Service serviceTwo = keys.get(1);
        Service serviceThree = keys.get(2);
        Service serviceFour = keys.get(3);
        Service serviceFive = keys.get(4);
        assertThat(keys.size(), is(5));

        assertThat(serviceOne.getVersion(), is("2.3-SNAPSHOT"));
        assertThat(serviceTwo.getVersion(), is("2.2-SNAPSHOT"));
        assertThat(serviceThree.getVersion(), is("2.1-SNAPSHOT"));
        assertThat(serviceFour.getVersion(), is("2.0-SNAPSHOT"));
        assertThat(serviceFive.getVersion(), is("1.0-SNAPSHOT"));
    }

    @Test
    public void registerHeartbeat() {
        //given
        Service serviceToRegister = prepareServiceToRegister();

        //when
        //register heartbeat with 2sek ttl
        service.registerHeartbeat(serviceToRegister,2);

        //then
        //check if key exists in redis
        RedisCommands<String, String> commands = getRedisCommands();
        List<String> keys = commands.keys("app:sample:localhost:2222:2.0-SNAPSHOT");
        assertEquals(1,keys.size());
        assertThat(keys.get(0),is("app:sample:localhost:2222:2.0-SNAPSHOT"));
    }

    @Test
    public void testGet100kKeys() {
        final long LIMIT = 500;
        final String PATTERN = "*test:pickme:*";
        add10kKeys();
        RedisCommands<String, String> commands = getRedisCommands();
        ScanIterator<String> scan = ScanIterator.scan(commands, ScanArgs.Builder.limit(LIMIT).match(PATTERN));

        List<String> keys = new ArrayList<>();
        while (scan.hasNext()) {
            keys.add(scan.next());
        }

        assertEquals(10,keys.size());
    }


    private Service prepareServiceToRegister() {
        HashMap<String, String> serviceKv = new HashMap<>();
        serviceKv.put("prefix", "app");
        serviceKv.put("name","sample");
        serviceKv.put("host", "localhost");
        serviceKv.put("port", "2222");
        serviceKv.put("version", "2.0-SNAPSHOT");
        serviceKv.put("loadFactor", "2");

        return new Service(serviceKv);
    }


    private void setupServerAndStart() {
         redisServer = RedisServer.builder()
                .port(REDIS_TEST_PORT)
                .setting(REDIS_MEMORY_HEAP) //maxheap 128M
                .build();
        redisServer.start();
    }

    private void addDummyData(){
        List<String> serviceNames = Arrays.asList(
                "app:user:localhost:8581:2.0-SNAPSHOT",
                "app:auth:localhost:8581:2.1-SNAPSHOT",
                "app:auth:localhost:2222:2.0-SNAPSHOT",
                "app:auth:localhost:1260:1.0-SNAPSHOT",
                "app:auth:localhost:1000:2.3-SNAPSHOT",
                "app:auth:localhost:9999:2.2-SNAPSHOT",
                "badly:formated:key-should:not-be:picked-up",
                "badly-formated:key2:should:not-be:picked-up");
        for(String keyName : serviceNames) {
            writeToRedis(keyName,20);
        }
    }

    private void writeToRedis(String key, long ttl) {
        String[] keyValues = key.split(":");
        RedisCommands<String, String> commands = getRedisCommands();
        HashMap<String,String> values = new HashMap<>();
        values.put("prefix",keyValues[0]);
        values.put("name",keyValues[1]);
        values.put("host",keyValues[2]);
        values.put("port",keyValues[3]);
        values.put("version",keyValues[4]);
        values.put("loadFactor","2");
        commands.hmset(key, values);
        commands.expire(key, ttl);
    }

    private void add10kKeys(){
        RedisCommands<String, String> commands = getRedisCommands();

        for (int i=0; i < 10000; i++) {
            String appName = "test";
            if((i%1000)==0){
                appName = "pickme";
            }
            HashMap<String,String> values = new HashMap<>();
            values.put("prefix","test");
            values.put("name",appName);
            values.put("host","localhost");
            values.put("port","0000");
            values.put("version",i + "-SNAPSHOT");
            values.put("loadFactor","1");
            String key = "test:" + appName + ":localhost:0000:" + i + "-SNAPSHOT";

            commands.hmset(key, values);
            commands.expire(key, 30);
        }
    }

    private RedisCommands<String, String> getRedisCommands() {
        StatefulRedisConnection<String, String> connection = this.connection.getConnection();
        return connection.sync();
    }


    private void flushTestData() {
        RedisCommands<String, String> commands = getRedisCommands();
        commands.flushall();
    }

}