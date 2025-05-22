package com.project.farmeasyportal.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationRequest {
    private int schemeId;
    private String bankId;
    private int age;
    private long amount;
    private int cibilScore;
    private long salary;
}
