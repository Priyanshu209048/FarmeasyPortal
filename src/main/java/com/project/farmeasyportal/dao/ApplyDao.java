package com.project.farmeasyportal.dao;

import com.project.farmeasyportal.entities.Apply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplyDao extends JpaRepository<Apply, Integer> {
    Apply findBySchemeId(String scheme);
    Apply findByFarmerId(String farmer);
    Apply findByBankId(String bank);
    List<Apply> findAllBySchemeId(String scheme);
    List<Apply> findAllByFarmerId(String farmer);
    List<Apply> findAllByBankId(String bank);
    long countByFarmerId(String farmer);
}
