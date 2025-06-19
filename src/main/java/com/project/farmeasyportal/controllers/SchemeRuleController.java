package com.project.farmeasyportal.controllers;

import com.project.farmeasyportal.dao.BankDao;
import com.project.farmeasyportal.dao.SchemeRuleDao;
import com.project.farmeasyportal.dao.UserDao;
import com.project.farmeasyportal.entities.Bank;
import com.project.farmeasyportal.entities.SchemeRule;
import com.project.farmeasyportal.services.impl.DroolsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/rules")
@RequiredArgsConstructor
@Log4j2
public class SchemeRuleController {

    private final UserDao userDao;
    private final BankDao bankDao;
    private final SchemeRuleDao ruleDao;
    private final DroolsService droolsService;

    // Create a new rule
    @PostMapping("/create")
    public ResponseEntity<?> createRule(@RequestBody SchemeRule rule, Authentication authentication) throws IOException {
        Bank bank = bankDao.findByEmail(authentication.getName());

        log.info(rule);
        Boolean existingRule = ruleDao.existsByFieldAndOperatorAndValue(rule.getField(), rule.getOperator(), rule.getValue());
        if (existingRule) {
            return new ResponseEntity<>("Rules already Created", HttpStatus.BAD_REQUEST);
        }

        droolsService.createRule(rule, bank.getId(), rule.getSchemeId());
        ruleDao.save(rule);

        return new ResponseEntity<>("Rules Created", HttpStatus.CREATED);
    }

    /*// Get all rules for a specific scheme
    @GetMapping("/scheme/{schemeId}")
    public ResponseEntity<List<SchemeRule>> getRulesByScheme(@PathVariable int schemeId) {
        List<SchemeRule> rules = schemeRuleService.getRulesByScheme(schemeId);
        return ResponseEntity.ok(rules);
    }

    // Delete a rule by ID
    @DeleteMapping("/{ruleId}")
    public ResponseEntity<String> deleteRule(@PathVariable int ruleId) {
        schemeRuleService.deleteRule(ruleId);
        return ResponseEntity.ok("Rule deleted successfully");
    }*/
}
