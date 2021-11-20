package com.caito.userservice.feingclients;

import com.caito.userservice.models.Car;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "car-service", url = "http://localhost:8082")
@RequestMapping("/car")
public interface CarFeingClient {

    //Metodo para crear un car desde user-sevice

    @PostMapping
    public Car save(@RequestBody Car car);

    @GetMapping("/byuser/{userId}")
    public List<Car> getCars(@PathVariable Long userId);

}
