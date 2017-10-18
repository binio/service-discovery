package util;

import domain.Service;

import java.util.*;
import java.util.stream.Collectors;

public class ServiceUtils {

    public static List<Service> sortServices(List<Service> services) {
        Comparator<Service> byServiceVersion = Comparator.comparing(Service::getVersion);
        Comparator<Service> byLoadFactor = Comparator.comparing(Service::getLoadFactor);

        List<Service> list = services.stream()
                .sorted(byServiceVersion.thenComparing(byLoadFactor))
                .collect(Collectors.toList());
        Collections.reverse(list);
        return list;
    }
}

