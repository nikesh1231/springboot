package com.evbackend.enumerate;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum VehicleColor {

    WHITE("white"),
    BLACK("black"),
    RED("red"),
    YELLOW("yellow"),
    GREEN("green"),
    GRAY("gray"),
    BLUE("blue"),
    PURPLE("purple");

    private String value;

    VehicleColor(final String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static VehicleColor decode(final String code) {
        return Stream.of(VehicleColor.values()).filter(targetEnum -> targetEnum.value.equals(code)).findFirst().orElse(null);
    }

    @Override
    public String toString() {
        return this.getValue();
    }



}