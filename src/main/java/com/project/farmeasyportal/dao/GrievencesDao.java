package com.project.farmeasyportal.dao;

import com.project.farmeasyportal.entities.Bank;
import com.project.farmeasyportal.entities.Grievences;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GrievencesDao extends JpaRepository<Grievences, Integer> {
    List<Grievences> findAllByBankId(String bankId);
}
