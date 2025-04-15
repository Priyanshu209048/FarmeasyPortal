package com.project.farmeasyportal.dao;

import com.project.farmeasyportal.entities.Apply;
import com.project.farmeasyportal.entities.Bank;
import com.project.farmeasyportal.entities.Farmer;
import com.project.farmeasyportal.entities.Scheme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplyDao extends JpaRepository<Apply, Integer> {
    /*Apply findByScheme(Scheme scheme);
    Apply findByFarmer(Farmer farmer);
    Apply findByBank(Bank bank);
    List<Apply> findAllByScheme(Scheme scheme);
    List<Apply> findAllByFarmer(Farmer farmer);
    List<Apply> findAllByBank(Bank bank);
    long countByFarmer(Farmer farmer);*/
}
