package service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;
import dao.RedisConnection;
import dao.RedisServiceDao;
import dao.ServiceDao;
import domain.Service;

import java.util.List;

public class ServiceDiscoverySdk {

    private ServiceDao serviceDao;

    public ServiceDiscoverySdk(RedisConnection connection){
        this(new RedisServiceDao(connection));
    }

    public ServiceDiscoverySdk(ServiceDao serviceDao) {
        this.serviceDao = serviceDao;
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


    public HttpResponse<JsonNode> invoke(String domain, String path, String body) {
        /*
        * 1. How to find out type of request PUT / GET /  POST
        * 2. What headers?
        * 3. Are parameters in the body?
        * */
        HttpResponse response = null;
        HttpRequest request = Unirest.post("http://localhost:9005/power/12")
                .header("accept", "application/json").getHttpRequest();
        try{
            response =  request.asJson();
        }catch(UnirestException e){
            e.printStackTrace();
        }
        return response;
    }

}
