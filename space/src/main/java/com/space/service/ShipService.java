package com.space.service;


import com.space.controller.ShipOrder;
import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.repository.OldShipRepository;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    private String orderField = "id";
    private long countOfShips = 0;
    private final ShipRepository shipRepository;


    @Autowired
    public ShipService(ShipRepository shipRepository) {
        this.shipRepository = shipRepository;
    }

    public List<Ship> getAllShips(Map<String, String[]> params) {

        if (params.containsKey("pageNumber") && params.containsKey("pageSize")) {
            pageNumber = Integer.parseInt((params.get("pageNumber")[0]));
            pageSize = Integer.parseInt((params.get("pageSize")[0]));
            pageOffset = pageNumber * pageSize;
            // orderField = params.get("order")[0].toLowerCase();
            orderField = ShipOrder.valueOf(params.get("order")[0]).getFieldName();

        }

        Sort sort = Sort.by(orderField);

        //List<Ship> ships = (List<Ship>) shipRepository.shipsForPage(pageSize, pageOffset, orderField);
        Page<Ship> shipsSearch = shipRepository.findAll(PageRequest.of(pageNumber, pageSize, sort));
        List<Ship> ships = shipsSearch.getContent();


        countOfShips = shipsSearch.getTotalElements();

        //countOfShips = shipRepository.count();
        return ships;
    }


    public long getCountShips() {
        return countOfShips;
    }

    public void deleteById(Long id) {
        shipRepository.deleteById(id);
    }

}
