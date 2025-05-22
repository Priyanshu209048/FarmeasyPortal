package com.project.farmeasyportal.dao;

import com.project.farmeasyportal.entities.Cibil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CibilDao extends JpaRepository<Cibil, Integer> {

    Cibil findByUserId(String farmerId);

}
