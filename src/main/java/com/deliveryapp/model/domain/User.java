package com.deliveryapp.model.domain;

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
@Table(name = User.USER_COLLECTION_NAME)
public class User {

    public static final String USER_COLLECTION_NAME = "user";
    public static final String USERNAME_FIELD = "username";
    public static final String USER_ADDRESS_FIELD = "userAddress";
    public static final String USER_TIMESLOT_FIELD = "userTimeslotId";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = USERNAME_FIELD, nullable = false)
    private String username;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = USER_ADDRESS_FIELD, nullable = false)
    private Address userAddress;
}
