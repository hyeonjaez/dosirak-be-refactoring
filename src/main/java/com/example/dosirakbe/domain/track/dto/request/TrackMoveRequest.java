package com.example.dosirakbe.domain.track.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class TrackMoveRequest {
    @NotNull
    @JsonDeserialize(using = NumberDeserializers.BigDecimalDeserializer.class)
    @Digits(integer = 10, fraction = 5)
    private BigDecimal shortestDistance;

    @NotNull
    @JsonDeserialize(using = NumberDeserializers.BigDecimalDeserializer.class)
    @Digits(integer = 10, fraction = 5)
    private BigDecimal moveDistance;
}
