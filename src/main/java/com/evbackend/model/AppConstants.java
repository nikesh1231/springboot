package com.evbackend.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AppConstants {

    public static final String STARTTIMEOFCHARGE = "startTimeOfCharge";
    public static final String USER_ID = "userId";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String SECURITY_SCHEME_NAME = "bearerAuth";
    public static final String SCHEME = "bearer";
    public static final String BEARER_FORMAT = "JWT";
    public static final String ACTIVE_USER = "activeUser";
    public static final String CHARGE_STATION_ID = "chargeStationId";
    public static final String CHARGE_SITE = "chargeSite";
    public static final String ACCOUNT_ID = "accountId";
    public static final String SITE_ID = "siteId";
    public static final String CHARGE_SITE_ID = "chargeSiteID";
    public static final String ERROR_MESSAGE_EMPTY_REQUEST_BODY = "Request body is empty. Please provide valid request body.";
    public static final String BEARER = "Bearer";
}
