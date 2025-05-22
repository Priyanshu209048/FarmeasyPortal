package com.project.farmeasyportal.services.impl;

import com.project.farmeasyportal.entities.Apply;
import com.project.farmeasyportal.entities.Cibil;
import com.project.farmeasyportal.payloads.EvaluationRequest;
import com.project.farmeasyportal.entities.LoanForm;

public class EvaluationRequestMapper {

    public static EvaluationRequest fromEntities(Cibil cibil, LoanForm loanForm, Apply apply) {
        EvaluationRequest request = new EvaluationRequest();
        request.setSchemeId(apply.getSchemeId());
        request.setBankId(apply.getBankId());
        request.setAge(loanForm.getAge());
        request.setAmount(Long.parseLong(apply.getAmount()));
        request.setCibilScore(cibil.getCibil_score());
        request.setSalary(loanForm.getSalary());
        return request;
    }
}
