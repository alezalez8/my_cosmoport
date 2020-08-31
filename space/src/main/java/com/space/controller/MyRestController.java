package com.space.controller;

import com.space.model.Ship;
import com.space.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
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
        Map<String, String[]> params = webRequest.getParameterMap();
        List<Ship> ships = shipService.getAllShips(params);
        return ships;
    }

//===================================================================
    /*@GetMapping()
    public List<Ship> getAllShips(Model model) {
        List<Ship>  allShips = shipService.getAllShips();
        //model.addAttribute("ships", ships);
        return allShips;
    }*/


    @GetMapping("/count")
    public long getCountShips() {
        System.out.println("второй метод сработал");
        return shipService.getCountShips();
    }

    @DeleteMapping("${id}")
    //public void deleteShipById(@RequestParam (value = "ID") Long id ) {
    public void deleteShipById(Long id ) {
        System.out.println("Заход в метод удаления");
        shipService.deleteById(id);
    }

}
