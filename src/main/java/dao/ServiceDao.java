package dao;

import domain.Service;
import io.lettuce.core.api.sync.RedisCommands;

import java.util.List;

public interface ServiceDao {
    public List<String> getAllServices();
    public List<String> getServicesByName(String name);
    public List<Service> getServiceByNameSorted(String name);
    public void registerService(Service service, long ttl);
}
