package com.deliveryapp.service;

import com.deliveryapp.model.domain.Delivery;
import com.deliveryapp.model.domain.Timeslot;
import com.deliveryapp.model.dto.delivery.create.CreateDeliveryDto;
import com.deliveryapp.model.dto.delivery.DeliveryResponseDto;
import com.deliveryapp.model.enums.StatusEnum;
import com.deliveryapp.model.transformer.DeliveryTransformer;
import com.deliveryapp.repository.DeliveryRepository;
import com.deliveryapp.repository.TimeslotRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final TimeslotRepository timeslotRepository;

    @Autowired
    public DeliveryServiceImpl(DeliveryRepository deliveryRepository, TimeslotRepository timeslotRepository) {
        this.deliveryRepository = deliveryRepository;
        this.timeslotRepository = timeslotRepository;
    }

    @Override
    public String bookDelivery(CreateDeliveryDto createDeliveryDto) throws Exception {
        log.info("deliveryService.bookDelivery: going to book delivery for username={}", createDeliveryDto.getUserRequestDto().getUsername());
        Optional<Timeslot> optionalTimeslot = timeslotRepository.findById(Long.parseLong(createDeliveryDto.getUsersSelectedTimeslot()));
        log.info("deliveryService.bookDelivery: going to fetch optionalTimeslot from db by usersSelectedTimeslot={}", createDeliveryDto.getUsersSelectedTimeslot());
        if (optionalTimeslot.isPresent()) {
            Timeslot timeslot = optionalTimeslot.get();
            String city = createDeliveryDto.getUserRequestDto().getUserAddress().getCity();
            log.info("deliveryService.bookDelivery: going to check timeslots limitation(max 2), current ={}", timeslot.getTimeslotLimit());
            if (timeslot.getTimeslotLimit() < 2) {
//                    todo? && getDailyDeliveries().size() < 10) {
                log.info("deliveryService.bookDelivery: checking if timeslot support delivery city location");
                if (timeslot.getSupportedAddresses().equals(city)) {
                    log.info("deliveryService.bookDelivery: preparing new delivery data object, delivery statusEnum={}", StatusEnum.DELIVERY_BOOKED);
                    Delivery delivery = Delivery.builder()
                            .statusEnum(StatusEnum.DELIVERY_BOOKED)
                            .selectedTimeslot(timeslot)
                            .build();
                    log.info("deliveryService.bookDelivery: going to update timeslot limitation in db");
                    timeslotRepository.setTimeslotLimitById((timeslot.getTimeslotLimit() + 1), timeslot.getId());
                    log.info("deliveryService.bookDelivery: going to save new delivery in db, delivery={}", delivery);
                    deliveryRepository.saveAndFlush(delivery);
                    log.info("deliveryService.bookDelivery: data was added to db, for username={} ", createDeliveryDto.getUserRequestDto().getUsername());
                } else {
                    log.error("deliveryService.bookDelivery: this timeslot not support delivery city={}", city);
                    throw new Exception("This timeslot not support delivery to your city.");
                }
            } else {
                log.error("deliveryService.bookDelivery: timeslot not available, already has 2 deliveries");
                throw new Exception("Timeslot not available, already has 2 deliveries.");
            }
        } else {
            log.error("deliveryService.bookDelivery: timeslot not found in db for username={}", createDeliveryDto.getUserRequestDto().getUsername());
            throw new Exception("Timeslot not found.");
        }
        return "Delivery Booked!";
    }

    @Override
    public DeliveryResponseDto completeDelivery(Long deliveryId) throws Exception {
        log.info("deliveryService.completeDelivery: going to complete delivery for deliveryId={}", deliveryId);
        Optional<Delivery> deliveryFromDb = deliveryRepository.findById(deliveryId);
        log.info("deliveryService.completeDelivery: going to fetch delivery from db by deliveryId={}", deliveryId);
        if (deliveryFromDb.isPresent()) {
            if (deliveryFromDb.get().getStatusEnum().equals(StatusEnum.DELIVERY_BOOKED)) {
                log.info("deliveryService.completeDelivery: going to update delivery statusEnum={}", StatusEnum.DELIVERY_COMPLETED);
                deliveryRepository.setDeliveryStatusEnumById(StatusEnum.DELIVERY_COMPLETED, deliveryId);
                log.info("deliveryService.completeDelivery: data was updated, for deliveryId={} ", deliveryId);
            } else {
                log.info("deliveryService.completeDelivery: delivery was not booked yet for deliveryId={}", deliveryId);
                throw new Exception("Something went wrong, please try again.");
            }
        } else {
            log.info("Delivery was not found in db");
            log.error("deliveryService.completeDelivery: deliveryId={} not found in db", deliveryId);
            throw new Exception("Something went wrong, please try again.");
        }
        return deliveryRepository.findById(deliveryId).map(DeliveryTransformer::deliveryToDeliveryResponseDto).get();
    }

    @Override
    public String cancelDelivery(Long deliveryId) throws Exception {
        log.info("deliveryService.cancelDelivery: going to cancel delivery for deliveryId={}", deliveryId);
        Optional<Delivery> deliveryFromDb = deliveryRepository.findById(deliveryId);
        log.info("deliveryService.cancelDelivery: going to fetch delivery from db by deliveryId={}", deliveryId);
        if (deliveryFromDb.isPresent()) {
            Optional<Timeslot> timeslotOptional = timeslotRepository.findById(deliveryFromDb.get().getSelectedTimeslot().getId());
            log.info("deliveryService.cancelDelivery: going to fetch timeslot from db by deliveryId={}", deliveryId);
            if (timeslotOptional.isPresent()) {
                Timeslot timeslot = timeslotOptional.get();
                log.info("deliveryService.cancelDelivery: going to update timeslot limitation in db");
//                todo? timeslot.setDeliveriesCount(timeslot.getDeliveriesCount() - 1);
                if (timeslot.getTimeslotLimit() >= 1) {
                    timeslotRepository.setTimeslotLimitById((timeslot.getTimeslotLimit() - 1), timeslot.getId());
                }
            }
            log.info("deliveryService.cancelDelivery: going to update delivery statusEnum={}", StatusEnum.DELIVERY_CANCELED);
            deliveryRepository.setDeliveryStatusEnumById(StatusEnum.DELIVERY_CANCELED, deliveryId);
            log.info("deliveryService.cancelDelivery: data was updated, delivery was canceled for deliveryId={} ", deliveryId);
        } else {
            log.error("deliveryService.bookDelivery: delivery not found in db for deliveryId={}", deliveryId);
            throw new Exception("Something went wrong, Delivery not found.");
        }
        return "Your delivery has been canceled!";
    }

    /**
     * Since 'holidayapi' not supported current year for free - I hardcoded data of last year - '2021'.
     * <p>
     * 'courierAPI.json' file contains those values:
     * LocalDateTime minTime = LocalDateTime.of(LocalDate.of(2021, 10, 1), LocalTime.MIN);
     * LocalDateTime maxTime = LocalDateTime.of(LocalDate.of(2021, 10, 1), LocalTime.MAX);
     */
    @Override
    public List<DeliveryResponseDto> getAllDailyDeliveries() throws Exception {
        log.info("deliveryService.getAllDailyDeliveries: going to prepare minTime & maxTime for all daily deliveries");
        LocalDateTime minTime = LocalDateTime.of(LocalDate.of(2021, LocalDate.now().getMonthValue(), LocalDate.now().getDayOfMonth()), LocalTime.MIN);
        LocalDateTime maxTime = LocalDateTime.of(LocalDate.of(2021, LocalDate.now().getMonthValue(), LocalDate.now().getDayOfMonth()), LocalTime.MAX);
        log.info("deliveryService.getAllDailyDeliveries: going to fetch all daily deliveries from db");
        List<Timeslot> timeslots = timeslotRepository.findAllByStartTimeBetweenAndEndTimeBetween(minTime, maxTime, minTime, maxTime);
        List<Long> deliveries = new ArrayList<>();
        if (timeslots != null && timeslots.size() > 0) {
            for (Timeslot t : timeslots) {
                deliveries.add(t.getId());
            }
        } else {
            log.error("deliveryService.getAllDailyDeliveries: data not found in db");
            throw new Exception("Something went wrong, please try again.");
        }
        log.info("deliveryService.getAllDailyDeliveries: going to fetch all daily deliveries");
        return DeliveryTransformer.deliveryListToDeliveryResponseDtoList(deliveryRepository.findAllBySelectedTimeslotIdIn(deliveries));
    }

    /**
     * Since 'holidayapi' not supported current year for free - I hardcoded data of last year - '2021'.
     * <p>
     * 'courierAPI.json' file contains those values:
     * LocalDateTime now = LocalDateTime.of(LocalDate.of(2021, 10, 1), LocalTime.now());
     */
    @Override
    public List<DeliveryResponseDto> getAllWeeklyDeliveries() throws Exception {
        log.info("deliveryService.getAllWeeklyDeliveries: going to prepare startOfWeek & endOfWeek for all daily deliveries");
        LocalDateTime now = LocalDateTime.of(LocalDate.of(2021, LocalDate.now().getMonthValue(), LocalDate.now().getDayOfMonth()), LocalTime.now());
        LocalDateTime startOfWeek = LocalDateTime.of(now.minusDays(now.getDayOfWeek().getValue()).toLocalDate(), LocalTime.MIN);
        LocalDateTime endOfWeek = LocalDateTime.of(now.plusDays(7 % now.getDayOfWeek().getValue()).toLocalDate(), LocalTime.MAX);
        log.info("deliveryService.getAllWeeklyDeliveries: going to fetch all weekly deliveries from db");
        List<Timeslot> timeslots = timeslotRepository.findAllByStartTimeBetweenAndEndTimeBetween(startOfWeek, endOfWeek, startOfWeek, endOfWeek);
        List<Long> deliveries = new ArrayList<>();
        if (timeslots != null && timeslots.size() > 0) {
            for (Timeslot t : timeslots) {
                deliveries.add(t.getId());
            }
        } else {
            log.error("deliveryService.getAllWeeklyDeliveries: data not found in db");
            throw new Exception("Something went wrong, please try again.");
        }
        log.info("deliveryService.getAllWeeklyDeliveries: going to fetch all weekly deliveries");
        return DeliveryTransformer.deliveryListToDeliveryResponseDtoList(deliveryRepository.findAllBySelectedTimeslotIdIn(deliveries));
    }
}
