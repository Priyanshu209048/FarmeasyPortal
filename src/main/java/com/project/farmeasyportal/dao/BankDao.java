package com.project.farmeasyportal.dao;

import com.project.farmeasyportal.entities.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankDao extends JpaRepository<Bank, String> {
    Bank findByEmail(String email);
}
