package com.deliveryapp.model.dto.delivery.create;

import com.deliveryapp.model.dto.user.UserRequestDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateDeliveryDto {
    @JsonProperty
    private UserRequestDto userRequestDto;
    @JsonProperty
    private String usersSelectedTimeslot;
    @JsonProperty
    private String userAddress;
}
