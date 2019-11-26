package com.stream.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * DeviceEventManager
 *   - Receives device events and aggregate those events.
 *   - Stores aggregated events based on their categories.
 *   - Report aggregated events. 
 */
public class DeviceEventManager {

    private static final Logger logger = LogManager.getLogger(DeviceEventHandler.class);

    // eventMap: { device: { title: { country: count } } }
    private Map<String, Map<String, Map<String, Long>>> eventMap = null;
    private static long windowLength = 1000L; // 1 Second
    private long windowStart = 0;

    public DeviceEventManager() {
        this.eventMap = new HashMap<>();
        this.windowStart = System.currentTimeMillis();
    }

    public void eventReceived(DeviceEvent event) {
        logger.debug("Adding new device event");
        if (event == null || !event.getSev().equals("success")) return;
        try {
            if (event.getTime() < this.windowStart) return;

            String device = event.getDevice();
            String title = event.getTitle();
            String country = event.getCountry();
            Map<String, Map<String, Long>> titleMap = null;
            Map<String, Long> countryMap = null;

            if (eventMap.containsKey(device)) {
                titleMap = eventMap.get(device);
            } else {
                titleMap = new HashMap<String, Map<String, Long>>();
                eventMap.put(device, titleMap);
            }
            if (titleMap.containsKey(title)) {
                countryMap = titleMap.get(title);
            } else {
                countryMap = new HashMap<String, Long>();
                titleMap.put(title, countryMap);
            }
            long count = countryMap.getOrDefault(country, 0L);
            countryMap.put(country, count + 1);
            logger.debug("DeviceEvent added: Device=\"{}\" Title=\"{}\" Country=\"{}\" Count={}",
                         device, title, country, count + 1);
        } catch (Exception ex) {
            logger.error("Failed to add device event: {}", ex);
        }
    }

    public List<AggregatedEvent> eventReport() {
        logger.debug("Reporting aggregated device events");
        List<AggregatedEvent> res = new ArrayList<>();
        try {
            this.eventMap.forEach((device, titleMap) -> {
                titleMap.forEach((title, countryMap) -> {
                    countryMap.forEach((country, count) -> {
                        res.add(new AggregatedEvent(device, count, title, country));
                    });
                });
            });
            logger.debug("Device events aggregated: Total={}", res.size());
            this.windowStart += 1000;
            this.eventMap.clear();
            logger.debug("Device event window updated: NewWindow={}", this.windowStart);
        } catch (Exception ex) {
            logger.error("Failed to aggregate device events: {}", ex);
        }
        return res;
    }
}