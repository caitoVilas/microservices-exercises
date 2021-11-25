package com.caito.userservice.controller;

import com.caito.userservice.entity.User;
import com.caito.userservice.models.Bike;
import com.caito.userservice.models.Car;
import com.caito.userservice.service.UserService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAll(){
        List<User> users = userService.getAll();
        if (users.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id){
        User user = userService.userById(id);
        if (user == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<User> save(@RequestBody User user){
        User userNew = userService.save(user);
        return ResponseEntity.ok(userNew);
    }

    @CircuitBreaker(name = "carsCB", fallbackMethod = "fallBackGetCars")
    @GetMapping("/cars/{userId}")
    public ResponseEntity<List<Car>> getCars(@PathVariable Long userId){
        User user = userService.userById(userId);
        if (user == null)
            return ResponseEntity.notFound().build();
        List<Car> cars = userService.getCars(userId);
        return ResponseEntity.ok(cars);
    }

    @CircuitBreaker(name = "bikesCB", fallbackMethod = "fallBackGetBikes")
    @GetMapping("/bikes/{userId}")
    public ResponseEntity<List<Bike>> getBikes(@PathVariable Long userId){
        User user = userService.userById(userId);
        if (user == null)
            return ResponseEntity.notFound().build();
        List<Bike> bikes = userService.getBikes(userId);
        return ResponseEntity.ok(bikes);
    }

    @CircuitBreaker(name = "carsCB", fallbackMethod = "fallBackSaveCars")
    @PostMapping("/savecar/{userId}")
    public ResponseEntity<Car> saveCar(@PathVariable Long userId, @RequestBody Car car){
        if (userService.userById(userId) == null)
            return ResponseEntity.notFound().build();
        Car carNew = userService.saveCar(userId, car);
        return ResponseEntity.ok(car);
    }

    @CircuitBreaker(name = "bikesCB", fallbackMethod = "fallBackSaveBikes")
    @PostMapping("/savebike/{userId}")
    public ResponseEntity<Bike> saveBike(@PathVariable Long userId, @RequestBody Bike bike){
        if (userService.userById(userId) == null)
            return ResponseEntity.notFound().build();
        Bike bikeNew = userService.saveBike(userId, bike);
        return ResponseEntity.ok(bike);
    }

    @CircuitBreaker(name = "allCB", fallbackMethod = "fallBackGetAll")
    @GetMapping("/getall/{userId}")
    public ResponseEntity<Map<String, Object>> getAllVehicles(@PathVariable Long userId){
        Map<String, Object> result = userService.getUserAndVehicles(userId);
        return ResponseEntity.ok(result);
    }

    private ResponseEntity<List<Car>> fallBackGetCars(@PathVariable Long userId, RuntimeException e){
        return new  ResponseEntity("El servicio de Car no puede responder en este momento", HttpStatus.OK);
    }

    private ResponseEntity<Car> fallBackSaveCars(@PathVariable Long userId, @RequestBody Car car, RuntimeException e){
        return new  ResponseEntity("El servicio de Car no puede responder en este momento", HttpStatus.OK);
    }

    private ResponseEntity<List<Bike>> fallBackGetBikes(@PathVariable Long userId, RuntimeException e){
        return new  ResponseEntity("El servicio de Bike no puede responder en este momento", HttpStatus.OK);
    }

    private ResponseEntity<Bike> fallBackSaveBikes(@PathVariable Long userId, @RequestBody Bike bike, RuntimeException e){
        return new  ResponseEntity("El servicio de Bike no puede responder en este momento", HttpStatus.OK);
    }

    private ResponseEntity<Map<String, Object>> fallBackGetAll(@PathVariable Long userId, RuntimeException e){
        return new  ResponseEntity("los servicios de Car y Bike no pueden responder en este momento", HttpStatus.OK);
    }
}
