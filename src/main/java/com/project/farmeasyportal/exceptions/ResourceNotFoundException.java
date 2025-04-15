package com.project.farmeasyportal.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceNotFoundException extends RuntimeException {
    private final String resourceName;
    private final String fieldName;
    private final String field;

    public ResourceNotFoundException(String resourceName, String fieldName, String field){
        super(String.format("%s not found with %s : %s", resourceName, fieldName, field));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.field = field;
    }
}
