package com.space.service;


import com.space.controller.ShipOrder;
import com.space.model.Ship;
import com.space.myexception.BadRequestException;
import com.space.myexception.ShipNotFoundException;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ShipService {
    private int pageNumber = 0;
    private int pageSize = 3;
    private String orderField = "id";
    private long countOfShips = 0;
    private final ShipRepository shipRepository;


    @Autowired
    public ShipService(ShipRepository shipRepository) {
        this.shipRepository = shipRepository;
    }

    //========================================= get all ships ===================================
    public List<Ship> getAllShips(Map<String, String[]> params) {
        if (params.containsKey("pageNumber") && params.containsKey("pageSize")) {
            pageNumber = Integer.parseInt((params.get("pageNumber")[0]));
            pageSize = Integer.parseInt((params.get("pageSize")[0]));
            orderField = ShipOrder.valueOf(params.get("order")[0]).getFieldName();
        }
        Sort sort = Sort.by(orderField);
        Page<Ship> shipsSearch = shipRepository.findAll(PageRequest.of(pageNumber, pageSize, sort));
        List<Ship> ships = shipsSearch.getContent();
        countOfShips = shipsSearch.getTotalElements();
        return ships;
    }

    //========================================= get count ships ==================================
    public long getCountShips() {
        return countOfShips;
    }

    //========================================= delete ships ===================================
    public void deleteById(Long id) {
        if (!isIdValid(id)) {
            throw new BadRequestException();
        }
        if (shipRepository.existsById(id)) {
            shipRepository.deleteById(id);
        } else throw new ShipNotFoundException("Ship not found");
    }

    //========================================= create ships ===================================
    public Ship  createNewShip(Ship ship) {
        checkShipParamsOfNull(ship);
        checkShipParamsOfBounds(ship);

        if (ship.getUsed() == null) {
            ship.setUsed(false);
        }


       /* Calendar calendar = Calendar.getInstance();
        calendar.setTime(ship.getProdDate());*/

        ship.setRating(calculateRating(ship));

        return shipRepository.save(ship);

    }

    //========================================= get ship by id =================================
    public Ship getShipsById(Long id) {
        if (id <= 0) {
            throw new BadRequestException();
        }
        if (!shipRepository.existsById(id))
            throw new ShipNotFoundException("Ship not found");
        return shipRepository.findById(id).get();
    }

    //========================================= check param ====================================
    private void checkShipParamsOfBounds(Ship ship) {

        if (ship.getName() != null && (ship.getName().length() < 1 || ship.getName().length() > 50))
            throw new BadRequestException("Incorrect Ship name");

        if (ship.getPlanet() != null && (ship.getPlanet().length() < 1 || ship.getPlanet().length() > 50))
            throw new BadRequestException("Incorrect Ship planet");

        if (ship.getCrewSize() != null && (ship.getCrewSize() < 1 || ship.getCrewSize() > 9999))
            throw new BadRequestException("Incorrect Ship crewSize");

        if (ship.getSpeed() != null && (ship.getSpeed() < 0.01D || ship.getSpeed() > 0.99D))
            throw new BadRequestException("Incorrect Ship speed");

        if (ship.getProdDate() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(ship.getProdDate());
            if (calendar.get(Calendar.YEAR) < 2800 || calendar.get(Calendar.YEAR) > 3019)
                throw new BadRequestException("Incorrect Ship date");
        }
    }

    //========================================= calcul rating ==================================
    private Double calculateRating(Ship ship) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(ship.getProdDate());
        int year = calendar.get(Calendar.YEAR);

        BigDecimal raiting = new BigDecimal((80 * ship.getSpeed() * (ship.getUsed() ? 0.5 : 1)) / (3019 - year + 1));
        raiting = raiting.setScale(2, RoundingMode.HALF_UP);
        return raiting.doubleValue();
    }

    //========================================= check id ==================================
    private Long checkIdValid(String id) {
        if (id == null || id.equals("") || id.equals("0"))
            throw new BadRequestException("Некорректный ID");

        try {
            Long validId = Long.parseLong(id);
            return validId;
        } catch (NumberFormatException e) {
            throw new BadRequestException("ID не является числом", e);
        }
    }

    //=========================================== check id ==================
    private Boolean isIdValid(Long id) {
        if (id == null ||
                id != Math.floor(id) ||
                id <= 0) {
            return false;
        }
        return true;
    }

    //=========================================== check params ==================
    private void checkShipParamsOfNull(Ship ship) {
        if (ship.getName() == null
                || ship.getPlanet() == null
                || ship.getShipType() == null
                || ship.getProdDate() == null
                || ship.getSpeed() == null
                || ship.getCrewSize() == null
        ) throw new BadRequestException("one or more parameters is null");
    }


}
