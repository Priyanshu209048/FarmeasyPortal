package com.project.farmeasyportal.dao;

import com.project.farmeasyportal.entities.Bank;
import com.project.farmeasyportal.entities.Scheme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SchemeDao extends JpaRepository<Scheme, Integer> {
    /*Scheme findByBank(Bank bank);
    List<Scheme> findAllByBank(Bank bank);*/
}
