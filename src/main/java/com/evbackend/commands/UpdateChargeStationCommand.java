package com.evbackend.commands;

import java.util.UUID;

import org.springframework.lang.Nullable;

public class UpdateChargeStationCommand {

    @Nullable
    String model;

    @Nullable
    String ip;

    @Nullable
    String host;

    @Nullable
    String password;

    @Nullable
    String version;

    @Nullable
    UUID siteId;
}
