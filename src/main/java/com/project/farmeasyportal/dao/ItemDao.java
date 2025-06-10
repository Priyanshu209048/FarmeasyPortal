package com.project.farmeasyportal.dao;

import com.project.farmeasyportal.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemDao extends JpaRepository<Item, Integer> {
    Item findByMerchantId(String id);
    List<Item> findAllByMerchantId(String id);
}
