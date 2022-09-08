package com.deliveryapp.service;

import com.deliveryapp.core.constant.HttpStatus;
import com.deliveryapp.model.domain.Timeslot;
import com.deliveryapp.repository.TimeslotRepository;
import com.github.agogs.holidayapi.api.APIConsumer;
import com.github.agogs.holidayapi.api.impl.HolidayAPIConsumer;
import com.github.agogs.holidayapi.model.Holiday;
import com.github.agogs.holidayapi.model.HolidayAPIResponse;
import com.github.agogs.holidayapi.model.QueryParams;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TimeslotServiceImpl implements TimeslotService {

    @Value("${holiday.api.uri}")
    private String EXTERNAL_URI;
    @Value("${holiday.api.key}")
    private String HOLIDAY_API_KEY;
    @Value("${courier.api.path}")
    private String COURIER_AI_PATH;

    private final TimeslotRepository timeslotRepository;

    @Autowired
    public TimeslotServiceImpl(TimeslotRepository timeslotRepository) {
        this.timeslotRepository = timeslotRepository;
    }

    @Override
    public void jsonFileCachingManager() throws FileNotFoundException, ParseException {
        try {
            log.info("timeslotService.jsonFileCachingManager: going to retrieve data from json static file: 'courierAPI.json'");
            JSONParser parser = new JSONParser(new FileReader(COURIER_AI_PATH));
            List<Map<String, Object>> list = (List<Map<String, Object>>) parser.parse();
            log.info("timeslotService.jsonFileCachingManager: parsing data from: 'courierAPI.json' to Timeslot model object");
            List<Timeslot> timeslots = list.stream().map(l ->
                    Timeslot.builder()
                            .startTime(LocalDateTime.parse(l.get("startTime").toString()))
                            .endTime(LocalDateTime.parse(l.get("endTime").toString()))
                            .supportedAddresses((l.get("supportedAddresses").toString()))
                            .build()).collect(Collectors.toList());
            log.info("timeslotService.jsonFileCachingManager: going to combine data: json static file: 'courierAPI.json' & external source: 'holidayapi'");
            timeslots.stream().filter(holiday -> !holidayApiManager(holiday)).forEach(timeslot -> timeslotRepository.saveAndFlush(timeslot));
        } catch (Exception e) {
            log.error("timeslotService.jsonFileCachingManager: IOException");
        }
    }

    @Override
    public boolean holidayApiManager(Timeslot timeslot) {
        try {
            log.info("timeslotService.holidayApiManager: going to retrieve data from external source: 'holidayapi' for timeslot={}", timeslot);
            APIConsumer consumer = new HolidayAPIConsumer(EXTERNAL_URI);
            QueryParams params = new QueryParams();
            LocalDate timeslotStartTime = timeslot.getStartTime().toLocalDate();
            log.info("timeslotService.holidayApiManager: resolving APIConsumer object to structured Timeslot model object, for timeslot={}", timeslot);
            params.key(HOLIDAY_API_KEY)
                    .format(QueryParams.Format.JSON)
                    .country(QueryParams.Country.ISRAEL)
                    .year(timeslotStartTime.getYear())
                    .month(timeslotStartTime.getMonthValue())
                    .pretty(true);
            log.info("timeslotService.holidayApiManager: preparing HolidayAPIResponse object for timeslot={}", timeslot);
            HolidayAPIResponse response = consumer.getHolidays(params);
            if (response.getStatus() == HttpStatus.HTTP_STATUS_OK) {
                List<Holiday> holidays = response.getHolidays();
                return holidays.stream().anyMatch(holiday -> LocalDate.parse(holiday.getDate()).equals(timeslotStartTime));
            }
        } catch (IOException e) {
            log.error("timeslotService.holidayApiManager: IOException, timeslot={}", timeslot);
        }
        return false;
    }

    @Override
    public List<Timeslot> retrieveAllAvailableTimeslots(String supportedAddresses) {
        log.info("timeslotService.retrieveAllAvailableTimeslots: going to fetch all available timeslot from db");
        return timeslotRepository.findAll().stream()
                .filter(timeslot -> timeslot.getSupportedAddresses().contains(supportedAddresses))
                .filter(timeslot -> timeslot.getTimeslotLimit() < 2)
                .collect(Collectors.toList());
    }
}
