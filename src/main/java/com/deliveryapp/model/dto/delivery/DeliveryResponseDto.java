package com.deliveryapp.model.dto.delivery;

import com.deliveryapp.model.domain.Timeslot;
import com.deliveryapp.model.enums.StatusEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeliveryResponseDto {
    @JsonProperty
    private StatusEnum statusEnum;
    @JsonProperty
    private Timeslot selectedTimeslot;
}
