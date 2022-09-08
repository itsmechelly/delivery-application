package com.deliveryapp.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Version;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = Timeslot.TIMESLOT_COLLECTION_NAME)
public class Timeslot {

    public static final String TIMESLOT_COLLECTION_NAME = "timeslot";
    public static final String START_TIME_FIELD = "startTime";
    public static final String END_TIME_FIELD = "endTime";
    public static final String SUPPORTED_ADDRESS = "supportedAddresses";
    public static final String TIMESLOT_LIMIT_FILED = "timeslotLimit";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = START_TIME_FIELD, nullable = false)
    private LocalDateTime startTime;

    @Column(name = END_TIME_FIELD, nullable = false)
    private LocalDateTime endTime;

    /**
     * supportedAddresses => address->city
     */
    @Column(name = SUPPORTED_ADDRESS, nullable = false)
    private String supportedAddresses;

    @Builder.Default
    @Column(name = TIMESLOT_LIMIT_FILED, nullable = false)
    private Integer timeslotLimit = 0;

    @Version
    private Long version;
}
