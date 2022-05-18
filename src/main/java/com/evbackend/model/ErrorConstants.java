package com.evbackend.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorConstants {

    public static final String TOKEN_PARSE_EXCEPTION = "Token parse exception";
}
