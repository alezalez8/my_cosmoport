package com.space.repository;

import com.space.model.Ship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface ShipRepository extends JpaRepository<Ship, Long>, JpaSpecificationExecutor<Ship> {}
//public interface ShipRepository extends PagingAndSortingRepository<Ship, Long>, JpaSpecificationExecutor<Ship> {}


