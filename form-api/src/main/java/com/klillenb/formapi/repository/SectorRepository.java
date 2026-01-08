package com.klillenb.formapi.repository;

import com.klillenb.formapi.model.Sector;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SectorRepository extends JpaRepository<Sector, Long> {

    List<Sector> findByParentIsNull();
}
