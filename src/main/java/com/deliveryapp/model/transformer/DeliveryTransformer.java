package com.deliveryapp.model.transformer;

import com.deliveryapp.model.domain.Delivery;
import com.deliveryapp.model.dto.delivery.DeliveryResponseDto;

import java.util.List;
import java.util.stream.Collectors;

public class DeliveryTransformer {

    public static DeliveryResponseDto deliveryToDeliveryResponseDto(Delivery delivery) {
        return DeliveryResponseDto
                .builder()
                .statusEnum(delivery.getStatusEnum())
                .selectedTimeslot(delivery.getSelectedTimeslot())
                .build();
    }

    public static List<DeliveryResponseDto> deliveryListToDeliveryResponseDtoList(List<Delivery> deliveries) {
        return deliveries.stream().map(delivery -> DeliveryResponseDto
                        .builder()
                        .statusEnum(delivery.getStatusEnum())
                        .selectedTimeslot(delivery.getSelectedTimeslot())
                        .build())
                .collect(Collectors.toList());
    }
}
