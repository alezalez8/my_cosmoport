package com.space.repository;

import com.space.model.Ship;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface ShipRepository extends CrudRepository<Ship, Long> {

    //select * from ship order by id limit 2 offset 2;
    @Query(nativeQuery = true, value = "select * from ship  order by :field ASC limit :pageSize  offset :pageOffset")
    public List<Ship> shipsForPage(@Param("pageSize") int pageSize,
                                   @Param("pageOffset")long pageOffset,
                                   @Param("field")String field);


}
