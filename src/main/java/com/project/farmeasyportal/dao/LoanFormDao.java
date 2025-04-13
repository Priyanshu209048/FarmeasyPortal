package com.project.farmeasyportal.dao;

import com.project.farmeasyportal.entities.LoanForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanFormDao extends JpaRepository<LoanForm, Integer> {
    Boolean existsByEmail(String email);
    LoanForm findByEmail(String email);
}
