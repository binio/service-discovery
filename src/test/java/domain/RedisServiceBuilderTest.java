package domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class RedisServiceBuilderTest {

    @Test
    public void getKeyFromHeartBeat() throws Exception {
        HashMap<String, String> serviceKv = new HashMap<>();
        serviceKv.put("prefix", "app");
        serviceKv.put("name","sample");
        serviceKv.put("host", "localhost");
        serviceKv.put("port", "1250");
        serviceKv.put("version", "1.0-SNAPSHOT");
        serviceKv.put("loadFactor", "2");

        String key = new Service(serviceKv).getKey();
        assertThat(key, is("app:sample:localhost:1250:1.0-SNAPSHOT"));

    }

}