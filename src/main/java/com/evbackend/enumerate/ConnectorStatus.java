package com.evbackend.enumerate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum ConnectorStatus {

    AVAILABLE("available"),
    PREPARING("preparing"),
    RESERVED("reserved"),
    UNAVAILABLE("unavailable");

    private String value;

    ConnectorStatus(final String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static ConnectorStatus decode(final String code) {
        return Stream.of(ConnectorStatus.values()).filter(targetEnum -> targetEnum.value.equals(code)).findFirst().orElse(null);
    }

    @Override
    public String toString() {
        return this.getValue();
    }



}
