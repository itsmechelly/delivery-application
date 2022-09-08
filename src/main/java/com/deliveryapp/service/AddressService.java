package com.deliveryapp.service;

import com.deliveryapp.model.domain.Address;

import java.io.IOException;

public interface AddressService {

    Address resolveAddress(String searchTerm) throws IOException, InterruptedException;
}
