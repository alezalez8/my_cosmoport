package com.space.service;

import com.space.model.Ship;
import com.space.model.ShipType;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.Map;

public class ShipSpecification {
    private ShipsParameters parameters;

    public static Specification<Ship> filters(Map<String, String[]> params) {  // params contains all fields of ship

        return Specification.where(
                searchByName(params.get("name")).
                        and(searchByPlanet(params.get("planet")).
                                and(shipsByShipType(params.get("shipType")))
                                .and(shipsByUsage(params.get("isUsed")))
                                .and(shipsByDate(params.get("after"), params.get("before")))
                                .and(shipsByShipType(params.get("shipType")))
                                .and(shipsByCrewSize(params.get("minCrewSize"), params.get("maxCrewSize")))
                                .and(shipsBySpeed(params.get("minSpeed"), params.get("maxSpeed")))
                                .and(shipsByRating(params.get("minRating"), params.get("maxRating")))
                        )
        );

    }

    //=============================================== search by Name ====================================
    public static Specification<Ship> searchByName(String[] name) {
        return new Specification<Ship>() {
            @Override
            public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return name == null ? null : criteriaBuilder.like(root.get("name"), "%" + name[0] + "%");
            }
        };
    }

    //=============================================== search by Planet ==================================
    public static Specification<Ship> searchByPlanet(String[] planet) {
        return new Specification<Ship>() {
            @Override
            public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return planet == null ? null : criteriaBuilder.like(root.get("planet"), "%" + planet[0] + "%");
            }
        };
    }

    //=============================================== search by Date ====================================
    public static Specification<Ship> shipsByDate(String[] after, String[] before) {

        return (r, q, cb) -> {
            if (after == null && before == null) return null;
            if (after == null) {

                Date before1 = new Date(Long.parseLong(before[0]));
                return cb.lessThanOrEqualTo(r.get("prodDate"), before1);
            }
            if (before == null) {
                Date after1 = new Date(Long.parseLong(after[0]));
                return cb.greaterThanOrEqualTo(r.get("prodDate"), after1);
            }
            Date before1 = new Date(Long.parseLong(before[0]));
            Date after1 = new Date(Long.parseLong(after[0]));
            return cb.between(r.get("prodDate"), after1, before1);
        };
    }

    //=============================================== search by isUsed ==================================
    public static Specification<Ship> shipsByUsage(String[] isUsed) {
        return (r, q, cb) -> {
            if (isUsed == null) return null;
            Boolean isUsed1 = Boolean.valueOf(isUsed[0]);
            if (isUsed1) return cb.isTrue(r.get("isUsed"));
            else return cb.isFalse(r.get("isUsed"));
        };
    }

    //=============================================== search by crew ====================================
    public static Specification<Ship> shipsByCrewSize(String[] min, String[] max) {
        return (root, query, cb) -> {
            if (min == null && max == null) return null;
            if (min == null)                return cb.lessThanOrEqualTo(root.get("crewSize"), Integer.parseInt(max[0]));
            if (max == null)                return cb.greaterThanOrEqualTo(root.get("crewSize"), Integer.parseInt(min[0]));
            return cb.between(root.get("crewSize"), Integer.parseInt(min[0]), Integer.parseInt(max[0]));
        };
    }

    //=============================================== search by speed ===================================
    public static Specification<Ship> shipsBySpeed(String[] min, String[] max) {
        return (r, q, cb) -> {
            if (min == null && max == null) return null;
            if (min == null)                return cb.lessThanOrEqualTo(r.get("speed"), Double.parseDouble(max[0]));
            if (max == null)                return cb.greaterThanOrEqualTo(r.get("speed"), Double.parseDouble(min[0]));
            return cb.between(r.get("speed"), Double.parseDouble(min[0]), Double.parseDouble(max[0]));
        };
    }

    //=============================================== search by rating ==================================


    public static Specification<Ship> shipsByRating(String[] min, String[] max) {
        return (r, q, cb) -> {
            if (min == null && max == null) return null;
            if (min == null)                return cb.lessThanOrEqualTo(r.get("rating"), Double.parseDouble(max[0]));
            if (max == null)                return cb.greaterThanOrEqualTo(r.get("rating"), Double.parseDouble(min[0]));
            return cb.between(r.get("rating"), Double.parseDouble(min[0]), Double.parseDouble(max[0]));
        };
    }
    //=============================================== search by ship type ===============================
    public static Specification<Ship> shipsByShipType(String[] shipType) {
        return new Specification<Ship>() {
            @Override
            public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return shipType == null ? null : criteriaBuilder.equal(root.get("shipType"), ShipType.valueOf(shipType[0]));
            }
        };



   /* public static Specification<Ship> shipsByShipType(ShipType shipType) {
        return (r, q, cb) -> shipType == null ? null : cb.equal(r.get("shipType"), shipType);
    }*/

    }
} // ShipSpecification
