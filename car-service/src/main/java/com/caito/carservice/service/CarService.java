package com.caito.carservice.service;

import com.caito.carservice.entity.Car;
import com.caito.carservice.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class CarService {

    @Autowired
    private CarRepository carRepository;

    public List<Car> getAll(){
        return carRepository.findAll();
    }

    public Car carById(Long id){
        return carRepository.findById(id).orElse(null);
    }

    public Car save(Car car){
        Car carNew = carRepository.save(car);
        return carNew;
    }

    public List<Car> byUserId(Long userId){
        return carRepository.findByUserId(userId);
    }
}
