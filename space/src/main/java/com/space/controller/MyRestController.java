package com.space.controller;

import com.space.model.Ship;
import com.space.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rest/ships")
public class MyRestController {

    @Autowired
    private ShipService shipService;

    @GetMapping()
    //public List<Ship> getAllShips(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber) {
    public List<Ship> getAllShips(WebRequest webRequest) {
        Map<String, String[]> params = webRequest.getParameterMap();
        List<Ship> ships = shipService.getAllShips(params);

        //System.out.println("первый метод сработал");
        //System.out.println("pageNumber =  " + pageNumber);
        return ships;
    }

    @GetMapping("/count")
    public long getCountShips() {
        System.out.println("второй метод сработал");
        return shipService.getCountShips();
    }
}
