package dao;

import domain.Service;

import java.util.List;

public interface ServiceDao {
    List<String> getAllServices();
    List<String> getServicesByName(String name);
    List<Service> getServiceByNameSorted(String name);
    void registerService(Service service, long ttl);
}
