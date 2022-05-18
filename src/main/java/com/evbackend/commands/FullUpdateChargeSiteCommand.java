package com.evbackend.commands;

import lombok.*;
import org.springframework.lang.NonNull;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Getter
@Setter
@ToString
public class FullUpdateChargeSiteCommand {

    @NonNull
    String siteName;

    @NonNull
    CreateAddressCommand address;

    @NonNull
    UUID accountId;

    @NonNull
    boolean isActiveSite;

}
