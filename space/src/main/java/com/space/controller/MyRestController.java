package com.space.controller;

import com.space.model.Ship;
import com.space.myexception.BadRequestException;
import com.space.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    //public List<Ship> getAllShips(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber) {
    public List<Ship> getAllShips(WebRequest webRequest) {
        //Pageable pageable = PageRequest.of(int page, int size, Sort.by(org.springframework.data.domain.Sort.Order.asc(id)))

        Map<String, String[]> params = webRequest.getParameterMap();
        List<Ship> ships = shipService.getAllShips(params);
        return ships;
    }
    //======================================== get count ===================

    @GetMapping("/ships/count")
    public long getCountShips() {
        System.out.println("второй метод сработал");
        return shipService.getCountShips();
    }

    //========================================= create =====================

    @PostMapping(value = "/ships", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void createNewShip(@RequestBody Ship ship) {
        //System.out.println("Crew: " + ship.getCrewSize());
        //System.out.println("Name: " + ship.getName());
       // System.out.println("Planet: " + ship.getPlanet());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(ship.getProdDate());
        System.out.println("year of made: " +  calendar.get(Calendar.YEAR));
        shipService.createNewShip(ship);

    }

    //============================================ delete ===================

    @DeleteMapping(value = "/ships/{id}")
    public void deleteShipById(@PathVariable(value = "id") Long id) {
        // add check for id and exception
        //Long longId = checkId(id);
        shipService.deleteById(id);
    }

    //========================================== edit ==================
    @GetMapping(value = "/ships/{id}")
    public Ship getShipById(@PathVariable(value = "id") Long id) {
        Ship ship1 = shipService.getShipsById(id);

        return ship1;

    }


    //=========================================== check id ==================
    public Long checkId(String id) {
        if (id == null || id.equals("") || id.equals("0"))
            throw new BadRequestException("Uncorrect ID");
        try {
            Long longId = Long.parseLong(id);
            return longId;
        } catch (NumberFormatException e) {
            throw new BadRequestException("ID not digit", e);
        }

    }

}
