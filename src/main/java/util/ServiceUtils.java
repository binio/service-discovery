package util;

import domain.RedisServiceBuilder;
import domain.Service;

import java.util.*;

public class ServiceUtils {

    public static String setKeyFromHeartBeat(Service service){
        RedisServiceBuilder redisServiceBuilder = new RedisServiceBuilder();
        return redisServiceBuilder.getKeyFromHeartBeat(service);
    }

    public static Map<String, String> setKeyValues(Service service) {
        HashMap values = new HashMap();
        values.put("prefix", service.getPrefix());
        values.put("name", service.getName());
        values.put("host", service.getHost());
        values.put("port", service.getPort());
        values.put("version", service.getVersion());
        values.put("loadFactor", service.getLoadFactor());
        return values;
    }

    public static List<Service> sortServices(List<Service> services) {
        services.sort(Comparator.comparing(Service::getVersion));
        Collections.reverse(services);
        return services;
    }
}

