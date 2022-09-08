package com.deliveryapp.service;

import com.deliveryapp.model.domain.Timeslot;
import org.apache.tomcat.util.json.ParseException;

import java.io.FileNotFoundException;
import java.util.List;

public interface TimeslotService {

    void jsonFileCachingManager() throws FileNotFoundException, ParseException;

    boolean holidayApiManager(Timeslot timeslot) throws Exception;

    List<Timeslot> retrieveAllAvailableTimeslots(String supportedAddresses);
}
