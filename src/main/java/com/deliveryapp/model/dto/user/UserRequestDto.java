package com.deliveryapp.model.dto.user;

import com.deliveryapp.model.domain.Address;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRequestDto {
    @JsonProperty
    private String username;
    @JsonProperty
    private Address userAddress;
}
