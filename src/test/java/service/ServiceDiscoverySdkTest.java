package service;

import dao.RedisConnection;
import domain.HeartBeat;
import domain.Service;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import redis.embedded.RedisServer;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static io.lettuce.core.SetArgs.Builder.ex;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;


@RunWith(MockitoJUnitRunner.class)
public class ServiceDiscoverySdkTest {

    RedisServer redisServer;
    RedisConnection connection;
    ServiceDiscoverySdk service;

    public  static final String REDIS_TEST_HOST = "localhost";
    public  static final String REDIS_MEMORY_HEAP = "maxmemory 128M";
    public  static final Integer REDIS_TEST_PORT = 6370;

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
        assertThat(serviceOne.getVersion(), is("2.3-SNAPSHOT"));

    }

    @Test
    public void registerHeartbeat() {
        //given
        HeartBeat heartbeat = prepareHeartBeat();

        //when
        //register heartbeat with 2sek ttl
        service.registerHeartbeat(heartbeat,2);

        //then
        //check if key exists in redis
        RedisCommands<String, String> commands = getRedisCommands();
        List<String> keys = commands.keys("app:sample:localhost:2222:2.0-SNAPSHOT");
        assertEquals(1,keys.size());
        assertThat(keys.get(0),is("app:sample:localhost:2222:2.0-SNAPSHOT"));
    }

    @Test //to do
    public void sortKeys() {
        List<String> keys = new ArrayList();
        keys.add("app:user:2.1-SNAPSHOT:wowcher.co.uk:8581");
        keys.add("app:user:2.0-SNAPSHOT:127.0.0.1:2222");
        keys.add("app:user:1.0-SNAPSHOT:localhost:1260");
        keys.add("app:auth:5.3-SNAPSHOT:0.0.0.0:1000");
        keys.add("app:auth:2.2-SNAPSHOT:192.168.0.0:9999");

        List k = keys.stream().sorted().collect(Collectors.toList());
        Collections.reverse(k);
        k.stream().forEach(o -> System.out.println(o));

        HeartBeat hb = prepareHeartBeat();
        service.registerHeartbeat(hb, 100);
        service.getHeartBeat("app:sample:localhost:2222:2.0-SNAPSHOT");
    }



    private HeartBeat prepareHeartBeat() {
        HeartBeat heartbeat = new HeartBeat();
        heartbeat.setPrefix("app");
        heartbeat.setName("sample");
        heartbeat.setHost("localhost");
        heartbeat.setPort("2222");
        heartbeat.setVersion("2.0-SNAPSHOT");
        return heartbeat;
    }


    private void setupServerAndStart() {
         redisServer = RedisServer.builder()
                .port(REDIS_TEST_PORT)
                .setting(REDIS_MEMORY_HEAP) //maxheap 128M
                .build();
        redisServer.start();
    }

    private void addDummyData(){

        RedisCommands<String, String> commands = getRedisCommands();
        commands.set("app:user:localhost:8581:2.0-SNAPSHOT","1", ex(2));
        commands.set("app:auth:localhost:8581:2.1-SNAPSHOT","1", ex(2));
        commands.set("app:auth:localhost:2222:2.0-SNAPSHOT","1", ex(2));
        commands.set("app:auth:localhost:1260:1.0-SNAPSHOT","1", ex(2));
        commands.set("app:auth:localhost:1000:2.3-SNAPSHOT","1", ex(2));
        commands.set("app:auth:localhost:9999:2.2-SNAPSHOT","1", ex(2));
        commands.set("badly-formated-key-should-not-be-picked-up","1", ex(2));
        commands.set("badly-formated-key2-should-not-be-picked-up","1", ex(2));

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