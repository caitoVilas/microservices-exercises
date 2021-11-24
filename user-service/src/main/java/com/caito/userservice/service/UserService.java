package com.caito.userservice.service;

import com.caito.userservice.entity.User;
import com.caito.userservice.feingclients.BikeFeignClient;
import com.caito.userservice.feingclients.CarFeingClient;
import com.caito.userservice.models.Bike;
import com.caito.userservice.models.Car;
import com.caito.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private CarFeingClient carFeingClient;
    @Autowired
    private BikeFeignClient bikeFeignClient;


    public List<User> getAll(){
        return userRepository.findAll();
    }

    public User userById(Long id){
        return userRepository.findById(id).orElse(null);
    }

    public User save(User user){
        User userNew = userRepository.save(user);
        return userNew;
    }

    public List<Car> getCars(Long userId){
        List<Car> cars = restTemplate.getForObject("http:/car-service/car/byuser/" + userId, List.class);
        return cars;
    }

    public List<Bike> getBikes(Long userId){
        List<Bike> bikes = restTemplate.getForObject("http://bike-service/bike/byuser/" + userId, List.class);
        return bikes;
    }

    public Car saveCar(Long userId,Car car){
        car.setUserId(userId);
        Car carNew = carFeingClient.save(car);
        return carNew;
    }

    public Bike saveBike(Long userId, Bike bike){
        bike.setUserId(userId);
        Bike bikeNew = bikeFeignClient.save(bike);
        return bikeNew;
    }

    public Map<String, Object> getUserAndVehicles(Long userId){

        Map<String, Object> result = new HashMap<>();
        User user = userRepository.findById(userId).orElse(null);
        if (user == null){
            result.put("Mensaje", "No existe el usuario");
            return result;
        }
        result.put("User", user);

       List<Car> cars = carFeingClient.getCars(userId);
       if (cars.isEmpty())
           result.put("Cars", "El usuario no tiene coches");
       else
           result.put("Cars", cars);

        List<Bike> bikes = bikeFeignClient.getBikes(userId);
        if (cars.isEmpty())
            result.put("Bikes", "El usuario no tiene motos");
        else
            result.put("Bikes", bikes);
        return result;
    }
}
