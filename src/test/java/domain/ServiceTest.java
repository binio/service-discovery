package domain;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ServiceTest {

    Service service;

    @Before
    public void setup() {
        Map<String,String> map = new HashMap<>();
        map.put("prefix","app");
        map.put("name","sample");
        map.put("host","localhost");
        map.put("port","1250");
        map.put("version","1.0-SNAPSHOT");
        map.put("loadFactor","2");

        service = new Service(map);
    }
    @Test
    public void testServiceObjectIsCreatedFromMap() {
        //given
        // I have map with K V
        Map<String,String> map = new HashMap<>();
        map.put("prefix","app");
        map.put("name","sample");
        map.put("host","localhost");
        map.put("port","1250");
        map.put("version","1.0-SNAPSHOT");
        map.put("loadFactor","2");

        //when
        //I call constructor passing map to it
        Service service = new Service(map);

        //then
        //service properties should be set
        assertThat(service.getPrefix(), is("app"));
        assertThat(service.getName(), is("sample"));
        assertThat(service.getHost(), is("localhost"));
        assertThat(service.getPort(), is("1250"));
        assertThat(service.getVersion(), is("1.0-SNAPSHOT"));
        assertThat(service.getLoadFactor(), is("2"));


    }

    @Test
    public void testGetMethods(){
        service.setPrefix("app-1");
        service.setName("sample-1");
        service.setHost("localhost-1");
        service.setPort("1250-1");
        service.setVersion("1.0-SNAPSHOT-1");
        service.setLoadFactor("2-1");

        assertThat(service.getPrefix(), is("app-1"));
        assertThat(service.getName(), is("sample-1"));
        assertThat(service.getHost(), is("localhost-1"));
        assertThat(service.getPort(), is("1250-1"));
        assertThat(service.getVersion(), is("1.0-SNAPSHOT-1"));
        assertThat(service.getLoadFactor(), is("2-1"));
    }


}