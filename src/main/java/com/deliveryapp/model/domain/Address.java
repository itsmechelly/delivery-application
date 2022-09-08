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
@Table(name = Address.ADDRESS_COLLECTION_NAME)
public class Address {

    public static final String ADDRESS_COLLECTION_NAME = "address";
    public static final String LINE_ONE_FIELD = "line1";
    public static final String LINE_TWO_FIELD = "line2";
    public static final String COUNTRY_FIELD = "country";
    public static final String REGION_FIELD = "region";
    public static final String CITY_FIELD = "city";
    public static final String STREET_FIELD = "street";
    public static final String HOME_NUMBER_FIELD = "homeNum";
    public static final String POST_CODE_FIELD = "postcode";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = LINE_ONE_FIELD, nullable = false)
    private String line1;

    @Column(name = LINE_TWO_FIELD, nullable = false)
    private String line2;

    @Column(name = COUNTRY_FIELD, nullable = false)
    private String country;

    @Column(name = REGION_FIELD, nullable = false)
    private String region;

    @Column(name = CITY_FIELD, nullable = false)
    private String city;

    @Column(name = STREET_FIELD, nullable = false)
    private String street;

    @Column(name = HOME_NUMBER_FIELD, nullable = false)
    private String homeNum;

    @Column(name = POST_CODE_FIELD, nullable = false)
    private String postcode;
}
