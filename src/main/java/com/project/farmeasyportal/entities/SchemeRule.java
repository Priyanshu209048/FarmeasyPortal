package com.project.farmeasyportal.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "scheme_rule")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SchemeRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String ruleName;

    @NotBlank(message = "Rule Field Cannot be Blank")
    @Column(name = "field", length = 20, nullable = false)
    private String field;

    @NotBlank(message = "Rule Operator Cannot be Blank")
    @Column(name = "operator", length = 20, nullable = false)
    private String operator;

    @NotBlank(message = "Rule Value Cannot be Blank")
    @Column(name = "value", length = 20, nullable = false)
    private String value;

    @Column(name = "scheme_id", nullable = false)
    private int schemeId;

}
