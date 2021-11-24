package com.caito.userservice.feingclients;

import com.caito.userservice.models.Bike;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "bike-service")
@RequestMapping("/bike")
public interface BikeFeignClient {

    @PostMapping
    public Bike save(@RequestBody Bike bike);

    @GetMapping("/byuser/{userId}")
    public List<Bike> getBikes(@PathVariable Long userId);



}
