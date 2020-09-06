package com.space.service;

import com.space.model.Ship;
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
                        )
        );

    }  //.    .and(shipsByDate(params.get("after",))

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
    public static Specification<Ship> shipsByDate(String[] afterIn, String[] beforeIn) {
        String afte = afterIn[0];
        Long after = Long.parseLong(afte);
        String befo = beforeIn[0];
        Long before = Long.parseLong(befo);
        return (r, q, cb) -> {
            if (after == null && before == null) return null;
            if (after == null) {

                Date before1 = new Date(before);
                return cb.lessThanOrEqualTo(r.get("prodDate"), before1);
            }
            if (before == null) {
                Date after1 = new Date(after);
                return cb.greaterThanOrEqualTo(r.get("prodDate"), after1);
            }
            Date before1 = new Date(before);
            Date after1 = new Date(after);
            return cb.between(r.get("prodDate"), after, before);
        };
    }

    //=============================================== search by crew ====================================
    //=============================================== search by speed ===================================
    //=============================================== search by rating ==================================
    //=============================================== search by isUsed ==================================
    public static Specification<Ship> shipsByUsage(String[] isUsed) {
        return (r, q, cb) -> {
            if (isUsed == null) return null;
            Boolean isUsed1 = Boolean.valueOf(isUsed[0]);
            if (isUsed1) return cb.isTrue(r.get("isUsed"));
            else return cb.isFalse(r.get("isUsed"));
        };
    }

    /*public static Specification<Ship> shipsByRating(Double min, Double max) {
        return (r, q, cb) -> {
            if (min == null && max == null) return null;
            if (min == null)                return cb.lessThanOrEqualTo(r.get("rating"), max);
            if (max == null)                return cb.greaterThanOrEqualTo(r.get("rating"), min);
            return cb.between(r.get("rating"), min, max);
        };
    }*/
    //=============================================== search by ship type ===============================
    public static Specification<Ship> shipsByShipType(String[] shipType) {
        //System.out.println("shiptype" + shipType[0].toString());
        return new Specification<Ship>() {
            @Override
            public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                // if(shipType != null){System.out.println("shiptype  " + shipType[0].toString());}
                return shipType == null ? null : criteriaBuilder.like(root.get("shipType"), shipType[0].toUpperCase());
            }
        };



   /* public static Specification<Ship> shipsByShipType(ShipType shipType) {
        return (r, q, cb) -> shipType == null ? null : cb.equal(r.get("shipType"), shipType);
    }*/

    }
} // ShipSpecification
