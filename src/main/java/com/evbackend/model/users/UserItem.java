package com.evbackend.model.users;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.UUID;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserItem {

    @Schema(description = "Unique id for user")
    UUID userId;

    @Schema(description = "Unique identifier the user chooses to login", example="sally01")
    String userName;

    @Schema(description = "User's first name", example = "Sally")
    String firstName;

    @Schema(description = "User's surname", example = "Smith")
    String lastName;

}
