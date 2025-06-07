package com.project.farmeasyportal.enums;

import lombok.Getter;

@Getter
public enum ItemCategory {
    TRACTOR(0),
    TEMPO(1),
    CULTIVATOR(2),
    PLOUGH(3),
    SPRAYER(4),
    OTHER(5);

    private final int code;

    ItemCategory(int code) {
        this.code = code;
    }

    public static ItemCategory fromCode(int code) {
        for (ItemCategory itemCategory : ItemCategory.values()) {
            if (itemCategory.getCode() == code) {
                return itemCategory;
            }
        }
        throw new IllegalArgumentException("Invalid status code: " + code);
    }
}

