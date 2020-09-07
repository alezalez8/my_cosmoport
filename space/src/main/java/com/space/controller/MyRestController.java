package com.space.controller;

import com.space.model.Ship;
import com.space.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rest")
public class MyRestController {

    private final ShipService shipService;

    @Autowired
    public MyRestController(ShipService shipService) {
        this.shipService = shipService;
    }


    //======================================= get All =====================
    @GetMapping("/ships")
    public List<Ship> getAllShips(WebRequest webRequest) {

        Map<String, String[]> params = webRequest.getParameterMap();
        List<Ship> ships = shipService.getAllShips(params);
        return ships;
    }
    //======================================== get count ===================

   /* @GetMapping("/ships/count")
    public long getCountShips() {
        return shipService.getCountShips();
    }*/

    //========================================= create =====================

    @PostMapping(value = "/ships", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Ship createNewShip(@RequestBody Ship ship) {
        return shipService.createNewShip(ship);
    }

    //============================================ delete ===================
    @DeleteMapping(value = "/ships/{id}")
    public void deleteShipById(@PathVariable(value = "id") String id) {
        shipService.deleteById(id);
    }

    //========================================== get ship by ID  ==================
    @GetMapping(value = "/ships/{id}")
    public Ship getShipById(@PathVariable(value = "id") String id) {
        //Ship ship = shipService.getShipsById(id);
        return shipService.getShipsById(id);
    }

    //========================================== edit ship ==================
    @PostMapping(value = "/ships/{id}")
    public Ship editShips(@RequestBody Ship ship, @PathVariable(value = "id") String id) {
        return shipService.editShip(ship, id);
    }

    //======================================= get count =====================
    @GetMapping("/ships/count")
    public Long getShipsCount(WebRequest webRequest) {

        Map<String, String[]> params = webRequest.getParameterMap();
        shipService.getAllShips(params);

        return shipService.getCountShips();
    }


}
