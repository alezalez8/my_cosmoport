package com.space.service;

import com.space.model.Ship;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Map;

public class ShipSpecification {

    public static Specification<Ship> filters(Map<String, String[]> params) {

        return Specification.where(
                searchByName(params.get("name")).
                        and(searchByPlanet(params.get("planet")))
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
    //=============================================== search by crew ====================================
    //=============================================== search by speed ===================================
    //=============================================== search by rating ==================================
    //=============================================== search by ship type ===============================


} // ShipSpecification
