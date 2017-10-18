package util;

import domain.Service;

import java.util.*;

public class ServiceUtils {

    public static List<Service> sortServices(List<Service> services) {
        services.sort(Comparator.comparing(Service::getVersion));
        Collections.reverse(services);
        return services;
    }
}

