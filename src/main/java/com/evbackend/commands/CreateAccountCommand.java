package com.evbackend.commands;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class CreateAccountCommand {

    @NonNull
    @Schema(description = "Account Name", example ="Account 1")
    String accountName;

}
