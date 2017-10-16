package domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class RedisServiceBuilderTest {


    @Test
    public void getKey() throws Exception {
        RedisServiceBuilder builder = new RedisServiceBuilder();
        String key = builder.setPrefix("app").setName("sample").setHost("localhost").setPort("1250").setVersion("1.0-SNAPSHOT").getKey();
        assertThat(key, is("app:sample:localhost:1250:1.0-SNAPSHOT"));
    }


    @Test
    public void getKeyFromHeartBeat() throws Exception {
        RedisServiceBuilder builder = new RedisServiceBuilder();

        HashMap<String, String> serviceKv = new HashMap<>();
        serviceKv.put("prefix", "app");
        serviceKv.put("name","sample");
        serviceKv.put("host", "localhost");
        serviceKv.put("port", "1250");
        serviceKv.put("version", "1.0-SNAPSHOT");
        serviceKv.put("loadFactor", "2");

        String key = builder.getKeyFromHeartBeat(new Service(serviceKv));
        assertThat(key, is("app:sample:localhost:1250:1.0-SNAPSHOT"));

    }

}