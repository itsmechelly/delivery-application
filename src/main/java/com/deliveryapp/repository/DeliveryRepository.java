package com.deliveryapp.repository;

import com.deliveryapp.model.domain.Delivery;
import com.deliveryapp.model.enums.StatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    @Modifying
    @Query("update Delivery d set d.statusEnum = ?1 where d.id = ?2")
    void setDeliveryStatusEnumById(StatusEnum statusEnum, Long id);

    List<Delivery> findAllBySelectedTimeslotIdIn(List<Long> selectedTimeslots);
}
