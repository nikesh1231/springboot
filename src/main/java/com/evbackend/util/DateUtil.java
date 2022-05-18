package com.evbackend.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtil {

    public static LocalDateTime getDate(Long second) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(second), ZoneOffset.UTC);
    }

    public static Long toSecond(LocalDateTime dateTime) {
        return dateTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneOffset.UTC).toEpochSecond();
    }

}
