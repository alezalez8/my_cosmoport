package com.space.service;


import com.space.model.Ship;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ShipService {
    private int pageNumber = 0;
    private int pageSize = 3;
    private int pageOffset = 0;
    private String orderField = "ID";
    private long countOfShips = 0;


    @Autowired
    ShipRepository repo;

    public List<Ship> getAllShips(Map<String, String[]> params) {
        //List<Ship> ships = (List<Ship>) repo.findAll();
        if (params.containsKey("pageNumber") && params.containsKey("pageSize")) {
            pageNumber = Integer.parseInt((params.get("pageNumber")[0]));
            pageSize = Integer.parseInt((params.get("pageSize")[0]));
            pageOffset = pageNumber * pageSize;
            orderField = params.get("order")[0].toLowerCase();
            System.out.println("order = " + orderField);

        }
        countOfShips = repo.count();
        List<Ship> ships = (List<Ship>) repo.shipsForPage(pageSize, pageOffset, orderField);

        return ships;
    }

    public long getCountShips() {
        return countOfShips;
    }

}
