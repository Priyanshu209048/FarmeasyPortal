package com.project.farmeasyportal.dao;

import com.project.farmeasyportal.entities.Merchant;
import com.project.farmeasyportal.payloads.MerchantDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MerchantDao extends JpaRepository<Merchant, String> {

    Boolean existsByEmail(String email);
    Optional<Merchant> findByEmail(String email);

    MerchantDTO getMerchantById(String merchantId);
}
