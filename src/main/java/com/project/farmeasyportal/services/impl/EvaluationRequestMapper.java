package com.project.farmeasyportal.services.impl;

import com.project.farmeasyportal.entities.Apply;
import com.project.farmeasyportal.entities.EvaluationRequest;
import com.project.farmeasyportal.entities.LoanForm;

public class EvaluationRequestMapper {

    public static EvaluationRequest fromEntities(LoanForm loanForm, Apply apply) {
        EvaluationRequest request = new EvaluationRequest();
        request.setSchemeId(apply.getSchemeId());
        request.setBankId(apply.getBankId());
        request.setAge(loanForm.getAge());
        request.setAmount(Long.parseLong(apply.getAmount()));
        request.setCibilScore(loanForm.getCibil());
        request.setSalary(loanForm.getSalary());
        return request;
    }
}
