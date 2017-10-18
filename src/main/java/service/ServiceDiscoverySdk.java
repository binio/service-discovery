package service;

import dao.RedisConnection;
import dao.ServiceDao;
import dao.ServiceDaoImpl;
import domain.Service;

import java.util.List;


public class ServiceDiscoverySdk {

    private ServiceDao serviceDao;

    public ServiceDiscoverySdk(RedisConnection connection){
        this.serviceDao = new ServiceDaoImpl(connection);
    }

    public List<String> getAllServices() {
        return serviceDao.getAllServices();
    }

    public void registerHeartbeat(Service service, long ttl) {
        serviceDao.registerService(service, ttl);
    }


    public List<String> getServiceByName(String name) {
       return serviceDao.getServicesByName(name);
    }

    public List<Service> getServiceByNameSorted(String name) {
        return serviceDao.getServiceByNameSorted(name);
    }

}
