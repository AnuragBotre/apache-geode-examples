package com.trendcore.core.domain;

import java.util.Arrays;

public enum Gender {
    FEMALE("F"),
    MALE("M");

    private final String abbreviation;

    Gender(final String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public static Gender valueOfAbbreviation(String abbreviation) {
        for (Gender gender : values()) {
            if (gender.getAbbreviation().equalsIgnoreCase(abbreviation)) {
                return gender;
            }
        }

        throw new IllegalArgumentException(String.format(
                "the argument for abbreviation (%1$s) does not correspond to a valid Gender (%2$s)",
                abbreviation, Arrays.toString(Gender.values())));
    }

    public String getAbbreviation() {
        return abbreviation;
    }
}
