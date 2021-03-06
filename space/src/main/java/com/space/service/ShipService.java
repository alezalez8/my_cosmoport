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
import org.springframework.data.jpa.domain.Specification;
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
    private final int DEFAULT_PAGE_NUMBER = 0;
    private final int DEFAULT_PAGE_SIZE = 3;
    private final String DEFAULT_ORDER_FIELD = "id";
    //private final long DEFAULT_COUNT_OF_SHIPS = 0;

    private int pageNumber;
    private int pageSize;
    private String orderField;
    private long countOfShips = 0;
    private final ShipRepository shipRepository;


    @Autowired
    public ShipService(ShipRepository shipRepository) {
        this.shipRepository = shipRepository;
    }

    //========================================= get all ships ===================================
    public List<Ship> getAllShips(Map<String, String[]> params) {
        /*if (params.containsKey("pageNumber") && params.containsKey("pageSize")) {
            pageNumber = Integer.parseInt(params.get("pageNumber")[0]);
            pageSize = Integer.parseInt(params.get("pageSize")[0]);
            orderField = ShipOrder.valueOf(params.get("order")[0]).getFieldName();
        }*/
        if (params.containsKey("pageNumber")) {
            pageNumber = Integer.parseInt(params.get("pageNumber")[0]);
        } else {
            pageNumber = DEFAULT_PAGE_NUMBER;
        }

        if (params.containsKey("pageSize")) {
            pageSize = Integer.parseInt(params.get("pageSize")[0]);
        }  else {
            pageSize = DEFAULT_PAGE_SIZE;
        }

        if (params.containsKey("order")) {
            orderField = ShipOrder.valueOf(params.get("order")[0]).getFieldName();
        } else {
            orderField = DEFAULT_ORDER_FIELD;
        }

        Sort sort = Sort.by(orderField);
        Specification<Ship> filters = ShipSpecification.filters(params);
        Page<Ship> shipsSearch = shipRepository.findAll(filters, PageRequest.of(pageNumber, pageSize, sort));
        List<Ship> ships = shipsSearch.getContent();
        countOfShips = shipsSearch.getTotalElements();
        return ships;
    }

    //========================================= get count ships ==================================
    public long getCountShips() {
        return countOfShips;
    }

    //========================================= delete ships ===================================
    public void deleteById(String id) {
        Long longId = checkIdValid(id);
        if (!isIdValid(longId)) {
            throw new BadRequestException();
        }
        if (shipRepository.existsById(longId)) {
            shipRepository.deleteById(longId);
        } else throw new ShipNotFoundException("Ship not found");
    }

    //========================================= create ships ===================================
    public Ship createNewShip(Ship ship) {
        checkShipParamsOfNull(ship);
        checkShipParamsOfBounds(ship);

        if (ship.getUsed() == null) {
            ship.setUsed(false);
        }
        ship.setRating(calculateRating(ship));
        return shipRepository.save(ship);

    }

    //========================================= get ship by id ==========================

    public Ship getShipsById(String id) {
        Long longId = checkIdValid(id);
        if (longId <= 0) {
            throw new BadRequestException();
        }
        if (!shipRepository.existsById(longId))
            throw new ShipNotFoundException("Ship not found");
        return shipRepository.findById(longId).get();
    }

    //========================================= edit ship ==============================
    public Ship editShip(Ship ship, String id) {

        Long longId = checkIdValid(id);
        checkShipParamsOfBounds(ship);

        if (!shipRepository.existsById(longId))
            throw new ShipNotFoundException("Ship not found");

        Ship editedShip = shipRepository.findById(longId).get();

        if (ship.getName() != null)
            editedShip.setName(ship.getName());

        if (ship.getPlanet() != null)
            editedShip.setPlanet(ship.getPlanet());

        if (ship.getShipType() != null)
            editedShip.setShipType(ship.getShipType());

        if (ship.getProdDate() != null)
            editedShip.setProdDate(ship.getProdDate());

        if (ship.getSpeed() != null)
            editedShip.setSpeed(ship.getSpeed());

        if (ship.getUsed() != null)
            editedShip.setUsed(ship.getUsed());

        if (ship.getCrewSize() != null)
            editedShip.setCrewSize(ship.getCrewSize());


        //editedShip.setId(longId);


        editedShip.setRating(calculateRating(editedShip));

        return shipRepository.saveAndFlush(editedShip);
    }


    //========================================= check param ============================
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

    //========================================= calcul rating ==========================
    private Double calculateRating(Ship ship) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(ship.getProdDate());
        int year = calendar.get(Calendar.YEAR);

        BigDecimal raiting = new BigDecimal((80 * ship.getSpeed() * (ship.getUsed() ? 0.5 : 1)) / (3019 - year + 1));
        raiting = raiting.setScale(2, RoundingMode.HALF_UP);
        return raiting.doubleValue();
    }

    //========================================= check id String ==============================
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

    //=========================================== check id Long =============================
    private Boolean isIdValid(Long id) {
        if (id == null ||
                id != Math.floor(id) ||
                id <= 0) {
            return false;
        }
        return true;
    }

    //=========================================== check params =========================
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
