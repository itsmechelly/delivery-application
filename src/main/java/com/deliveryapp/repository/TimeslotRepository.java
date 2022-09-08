package com.deliveryapp.repository;

import com.deliveryapp.model.domain.Timeslot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Transactional
public interface TimeslotRepository extends JpaRepository<Timeslot, Long> {

    @Modifying
    @Query("update Timeslot t set t.timeslotLimit = ?1 where t.id = ?2")
    void setTimeslotLimitById(Integer timeslotLimit, Long id);

    List<Timeslot> findAllByStartTimeBetweenAndEndTimeBetween(LocalDateTime minStartTime, LocalDateTime maxStartTime, LocalDateTime minEndTime, LocalDateTime maxEndTime);
}
