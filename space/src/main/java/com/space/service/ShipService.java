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
    private final ShipRepository shipRepository;

    @Autowired
    public ShipService(ShipRepository shipRepository) {
        this.shipRepository = shipRepository;
        countOfShips = shipRepository.count();
    }

    //============================================
    public List<Ship> getAllShips(Map<String, String[]> params) {
        //List<Ship> ships = (List<Ship>) repo.findAll();
        if (params.containsKey("pageNumber") && params.containsKey("pageSize")) {
            pageNumber = Integer.parseInt((params.get("pageNumber")[0]));
            pageSize = Integer.parseInt((params.get("pageSize")[0]));
            pageOffset = pageNumber * pageSize;
            orderField = params.get("order")[0].toLowerCase();
        }

        countOfShips = shipRepository.count();
        List<Ship> ships = (List<Ship>) shipRepository.shipsForPage(pageSize, pageOffset, orderField);

        return ships;
    }
    //==========================================================
    /*public List<Ship> getAllShips() {
        List<Ship> allShips = (List<Ship>) shipRepository.findAll();
        countOfShips = shipRepository.count();
        return allShips;
    }*/


    public long getCountShips() {
        return countOfShips;
    }

    public void deleteById(Long id) {
        shipRepository.deleteById(id);
    }

}
