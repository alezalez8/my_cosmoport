package com.space.controller;

import com.space.model.Ship;
import com.space.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;


import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rest/ships")
public class MyRestController {

    private final ShipService shipService;

    @Autowired
    public MyRestController(ShipService shipService) {
        this.shipService = shipService;
    }


    //=======================================
    @GetMapping()
    //public List<Ship> getAllShips(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber) {
    public List<Ship> getAllShips(WebRequest webRequest) {

        //Pageable pageable = PageRequest.of(int page, int size, Sort.by(org.springframework.data.domain.Sort.Order.asc(id)))

        Map<String, String[]> params = webRequest.getParameterMap();
        List<Ship> ships = shipService.getAllShips(params);
        return ships;
    }

//===================================================================




    @GetMapping("/count")
    public long getCountShips() {
        System.out.println("второй метод сработал");
        return shipService.getCountShips();
    }

    @DeleteMapping(value = "/{id}")
    public void deleteShipById(@PathVariable(value = "id") Long id) {
        // add check for id and exception
        shipService.deleteById(id);
    }

}
