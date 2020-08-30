package com.space.model;

import javax.persistence.*;
import java.util.Date;

@Entity
//@Table(name = "ship")
public class Ship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String planet;
    //private ShipType shipType;
    //private Long prodDate;
    private String shipType;
    private Date prodDate;
    private Boolean isUsed;
    private Double speed;
    private Integer crewSize;
    private Double rating;


    /*public Ship() {

    }

    public Ship(Long id, String name,
                String planet, ShipType shipType, Long prodDate, Boolean isUsed,
                Double speed, Integer crewSize, Double rating) {
        this.id = id;
        this.name = name;
        this.planet = planet;
        this.shipType = shipType;
        this.prodDate = prodDate;
        this.isUsed = isUsed;
        this.speed = speed;
        this.crewSize = crewSize;
        this.rating = rating;
    }*/
}
