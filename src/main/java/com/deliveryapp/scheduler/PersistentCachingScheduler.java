package com.deliveryapp.scheduler;

import com.deliveryapp.service.TimeslotService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.io.IOException;

@Slf4j
@Component
@Transactional
public class PersistentCachingScheduler {

    private final TimeslotService timeslotService;

    @Autowired
    public PersistentCachingScheduler(TimeslotService timeslotService) {
        this.timeslotService = timeslotService;
    }

    @PostConstruct
    @Scheduled(cron = "${cron.expression}")
    public void timeslotCachingManager() throws IOException, ParseException {
        log.info("persistentCachingScheduler.timeslotCachingManager going to update Caching data from: timeslotService.jsonFileCachingManager");
        timeslotService.jsonFileCachingManager();
        log.info("persistentCachingScheduler.timeslotCachingManager: caching data was updated, see you again next week, at Sunday 00:00. (:");
    }
}
