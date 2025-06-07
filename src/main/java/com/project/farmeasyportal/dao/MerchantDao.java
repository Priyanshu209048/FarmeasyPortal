package com.project.farmeasyportal.dao;

import com.project.farmeasyportal.entities.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MerchantDao extends JpaRepository<Merchant, String> {

    Boolean existsByEmail(String email);
    Optional<Merchant> findByEmail(String email);

}
