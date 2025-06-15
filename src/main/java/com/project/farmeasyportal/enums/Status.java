package com.project.farmeasyportal.enums;

import lombok.Getter;

@Getter
public enum Status {
    PENDING(0),
    APPROVED(1),
    REJECTED(2),
    DEACTIVATED(3),
    PAID(4),
    SUCCESS(5),
    FAILED(6),
    COMPLETED(7),
    DELIVERED(8);

    private final int code;

    Status(int code) {
        this.code = code;
    }

    public static Status fromCode(int code) {
        for (Status status : Status.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid status code: " + code);
    }
}

