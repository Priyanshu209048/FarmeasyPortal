package com.project.farmeasyportal.services.impl;

import com.project.farmeasyportal.dao.*;
import com.project.farmeasyportal.entities.Apply;
import com.project.farmeasyportal.entities.Farmer;
import com.project.farmeasyportal.entities.LoanForm;
import com.project.farmeasyportal.entities.SchemeRule;
import com.project.farmeasyportal.enums.Status;
import com.project.farmeasyportal.entities.EvaluationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;

@Service
@Log4j2
@RequiredArgsConstructor
public class DroolsService {

    private final SchemeRuleDao schemeRuleRepository;
    private final LoanFormDao loanFormRepository;
    private final ApplyDao applyRepository;
    private final FarmerDao farmerDao;

    private final KieServices kieServices = KieServices.Factory.get();

    public void createRule(SchemeRule rule, String bankId, int schemeId) throws IOException {
        File ruleDir = new File("src/main/resources/rules/" + bankId + "/" + schemeId);
        ruleDir.mkdirs();

        String ruleFileName = rule.getRuleName() + ".drl";
        File ruleFile = new File(ruleDir, ruleFileName);

        if (ruleFile.exists()) {
            throw new RuntimeException("Rule already exists: " + ruleFile.getName());
        }

        try (FileWriter fileWriter = new FileWriter(ruleFile)) {
            fileWriter.write(generateDrlRule(rule));
        }

        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        kieFileSystem.write("rules/" + bankId + "/" + schemeId + "/" + ruleFileName, generateDrlRule(rule));
        KieBuilder builder = kieServices.newKieBuilder(kieFileSystem).buildAll();
        KieModule kieModule = builder.getKieModule();
        kieServices.newKieContainer(kieModule.getReleaseId());
    }

    public boolean executeRules(EvaluationRequest evaluationRequest, String farmerId) {
        Farmer farmer = this.farmerDao.findById(farmerId).orElseThrow(() -> new RuntimeException("Farmer Not Found"));
        LoanForm loanForm = loanFormRepository.findByEmail(farmer.getEmail());

        Apply apply = applyRepository.findByFarmerIdAndSchemeIdAndBankId(farmerId, evaluationRequest.getSchemeId(), evaluationRequest.getBankId());

        if (apply == null) {
            throw new RuntimeException("No Apply Record Found for this Farmer, Scheme, and Bank");
        }

        KieSession kieSession = reloadKieBase(evaluationRequest.getBankId(), evaluationRequest.getSchemeId());
        /*kieSession.insert(loanForm);*/
        kieSession.insert(apply);
        kieSession.insert(evaluationRequest);
        log.info("EvaluationRequest: {}", evaluationRequest);
        int firedRules = kieSession.fireAllRules();
        log.info("Rules loaded for bankId={}, schemeId={}", evaluationRequest.getBankId(), evaluationRequest.getSchemeId());
        log.info("Total rules: {}", kieSession.getKieBase().getKiePackages().size());

        kieSession.dispose();

        if (firedRules == 0 || apply.getStatus() == Status.REJECTED || apply.getStatus() == null) {
            apply.setStatus(Status.REJECTED);
            apply.setReview("Your form is rejected because your form didn't met the criteria");
            apply.setStatusDate(String.valueOf(LocalDate.now()));
        }
        applyRepository.save(apply);

        log.info("Total Rules Fired: {}", firedRules);
        return apply.getStatus() == Status.PENDING;
    }

    private long getTotalRules(String bankId, Integer schemeId){
        try {
            return Files.walk(Paths.get("src/main/resources/rules/" + bankId + "/" + schemeId + "/")).filter(Files::isRegularFile).count();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deactivateRule(String bankId, String schemeId, String field, String operator, String value) {
        String ruleFileName = field.toUpperCase() + "_" + operator + "_" + value + ".drl";
        File ruleFile = new File("src/main/resources/rules/" + bankId + "/" + schemeId + "/" + ruleFileName);

        if (ruleFile.exists()) {
            if (ruleFile.delete()) {
                log.info("Rule {} deactivated successfully.", ruleFileName);
            } else {
                throw new RuntimeException("Failed to deactivate rule: " + ruleFileName);
            }
        } else {
            throw new RuntimeException("Rule not found: " + ruleFileName);
        }
    }

    private String generateDrlRule(SchemeRule rule) {
        int formattedValue = Integer.parseInt(rule.getValue());

        boolean isRejectionRule = rule.getRuleName().toLowerCase().contains("reject");

        int salience = isRejectionRule ? 10 : 5;
        /*String status = isRejectionRule ? "Rejected" : "Approved";*/

        String thenPart = getString(rule, isRejectionRule, formattedValue);

        return String.format(
                """
                package rules;
        
                import com.project.farmeasyportal.entities.LoanForm;
                import com.project.farmeasyportal.entities.Apply;
                import com.project.farmeasyportal.enums.Status;
                import com.project.farmeasyportal.entities.EvaluationRequest;
        
                rule "%s"
                salience %d
                when
                    $evaluationRequest : EvaluationRequest(%s %s %s)
                    $apply : Apply()
                then
                    %s
                end
                """,
                rule.getRuleName(),
                salience,
                rule.getField(), rule.getOperator(), formattedValue,
                thenPart
        );
    }

    private static String getString(SchemeRule rule, boolean isRejectionRule, int formattedValue) {
        String thenPart;
        if (isRejectionRule) {
            thenPart = String.format("$apply.setStatus(Status.REJECTED);\n    System.out.println(\"Rule Matched: %s %s %s -> Rejected\");",
                    rule.getField(), rule.getOperator(), formattedValue);
        } else {
            thenPart = String.format(
                    "if ($apply.getStatus() == null || !$apply.getStatus().equals(Status.REJECTED)) {\n" +
                            "    $apply.setStatus(Status.PENDING);\n" +
                            "}\n    System.out.println(\"Rule Matched: %s %s %s -> PENDING\");",
                    rule.getField(), rule.getOperator(), formattedValue);
        }
        return thenPart;
    }


    private KieSession reloadKieBase(String bankId, int schemeId) {
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();

        try {
            Files.walk(Paths.get("src/main/resources/rules/" + bankId + "/" + schemeId + "/"))
                    .filter(Files::isRegularFile)
                    .forEach(path -> {
                        try {
                            String fileName = path.getFileName().toString()+".drl";
                            String relativePath = "src/main/resources/rules/" + bankId + "/" + schemeId + "/" + fileName;
                            kieFileSystem.write(relativePath, new String(Files.readAllBytes(path)));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        KieBuilder builder = kieServices.newKieBuilder(kieFileSystem).buildAll();
        KieModule kieModule = builder.getKieModule();
        return kieServices.newKieContainer(kieModule.getReleaseId()).newKieSession();
    }
}
