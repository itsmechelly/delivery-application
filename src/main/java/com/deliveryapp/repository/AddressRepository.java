package com.deliveryapp.repository;

import com.deliveryapp.model.domain.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
