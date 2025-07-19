package com.project.farmeasyportal.services.impl;

import com.project.farmeasyportal.constants.UsersConstants;
import com.project.farmeasyportal.dao.SchemeDao;
import com.project.farmeasyportal.entities.Apply;
import com.project.farmeasyportal.entities.EvaluationRequest;
import com.project.farmeasyportal.entities.LoanForm;
import com.project.farmeasyportal.entities.Scheme;
import com.project.farmeasyportal.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;

public class EvaluationRequestMapper {

    public static EvaluationRequest fromEntities(LoanForm loanForm, Apply apply, String schemeCode) {
        EvaluationRequest request = new EvaluationRequest();
        request.setSchemeCode(schemeCode);
        request.setBankId(apply.getBankId());
        request.setAge(loanForm.getAge());
        request.setAmount(Long.parseLong(apply.getAmount()));
        request.setCibilScore(loanForm.getCibil());
        request.setSalary(loanForm.getSalary());
        return request;
    }
}
