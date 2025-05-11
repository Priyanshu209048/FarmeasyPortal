package com.project.farmeasyportal.payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GrievencesRequestDTO {

    private String grievencesType;
    private String grievencesDescription;
    private String bankId;

}
