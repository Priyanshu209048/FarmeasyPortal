package com.project.farmeasyportal.dao;

import com.project.farmeasyportal.entities.SchemeRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SchemeRuleDao extends JpaRepository<SchemeRule, Integer> {

    List<SchemeRule> findBySchemeId(int schemeId);
    Boolean existsByFieldAndOperatorAndValue(String field, String operator, String value);

}
