package com.deliveryapp.model.domain;

import com.deliveryapp.model.enums.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = Delivery.DELIVERY_COLLECTION_NAME)
public class Delivery {

    public static final String DELIVERY_COLLECTION_NAME = "delivery";
    public static final String STATUS_FILED = "status";
    public static final String SELECTED_TIMESLOT_FILED = "selectedTimeslotId";
    public static final String DELIVERY_LIMIT_FILED = "deliveryLimit";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = STATUS_FILED, nullable = false)
    private StatusEnum statusEnum;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = SELECTED_TIMESLOT_FILED)
    private Timeslot selectedTimeslot;
}
