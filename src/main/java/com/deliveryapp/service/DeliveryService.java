package com.deliveryapp.service;

import com.deliveryapp.model.dto.delivery.create.CreateDeliveryDto;
import com.deliveryapp.model.dto.delivery.DeliveryResponseDto;

import java.util.List;

public interface DeliveryService {

    String bookDelivery(CreateDeliveryDto createDeliveryDto) throws Exception;

    DeliveryResponseDto completeDelivery(Long deliveryId) throws Exception;

    String cancelDelivery(Long deliveryId) throws Exception;

    List<DeliveryResponseDto> getAllDailyDeliveries() throws Exception;

    List<DeliveryResponseDto> getAllWeeklyDeliveries() throws Exception;
}
