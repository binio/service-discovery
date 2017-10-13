package domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

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
        Service service = new Service();
        service.setPrefix("app");
        service.setName("sample");
        service.setHost("localhost");
        service.setPort("1250");
        service.setVersion("1.0-SNAPSHOT");
        service.setLoadFactor("2");
        String key = builder.getKeyFromHeartBeat(service);
        assertThat(key, is("app:sample:localhost:1250:1.0-SNAPSHOT"));

    }

}