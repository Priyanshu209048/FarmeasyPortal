package com.project.farmeasyportal.dao;

import com.project.farmeasyportal.entities.Apply;
import com.project.farmeasyportal.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplyDao extends JpaRepository<Apply, Integer> {
    Apply findBySchemeId(int scheme);
    Apply findByFarmerId(String farmer);
    Apply findByBankId(String bank);
    List<Apply> findAllBySchemeId(int scheme);
    List<Apply> findAllByFarmerId(String farmer);
    List<Apply> findAllByBankId(String bank);
    //List<Apply> findAllByBankIdAndStatusNot(String bankId, Status status);
    List<Apply> findAllByBankIdAndStatusNotIn(String bankId, List<Status> status);
    long countByFarmerId(String farmer);

    List<Apply> findAllByFarmerIdAndStatus(String farmerId, Status status);

    Apply findByFarmerIdAndSchemeIdAndBankId(String farmerId, int schemeId, String bankId);

    Optional<Apply> findByFarmerIdAndSchemeId(String farmerId, Integer schemeId);

    long countByBankId(String id);

    long countByBankIdAndStatus(String bankId, Status status);
}
