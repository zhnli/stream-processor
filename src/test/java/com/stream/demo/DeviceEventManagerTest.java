package com.stream.demo;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.List;

class DeviceEventManagerTest {
 
    void sleep() {
        try {
            Thread.sleep(1000);
        } catch (Exception ex) {
        }
    }

    DeviceEvent genDeviceEvent(String device, String sev, String title, String country, long time) {
        DeviceEvent event = new DeviceEvent();
        event.setDevice(device);
        event.setSev(sev);
        event.setTitle(title);
        event.setCountry(country);
        event.setTime(time);
        return event;
    }

    @Test
    void successEventsTest() {
        // Success events should be included in aggregation.
        DeviceEventManager dem = new DeviceEventManager();
        dem.eventReceived(genDeviceEvent("xbox", "success", "Teapot", "US", System.currentTimeMillis()));
        dem.eventReceived(genDeviceEvent("xbox", "success", "Pinetree", "UK", System.currentTimeMillis()));

        List<AggregatedEvent> events = dem.eventReport();
        assertEquals(events.size(), 2);
    }

    @Test
    void failedEventsTest() {
        // Non-success events should not be included in aggregation.
        DeviceEventManager dem = new DeviceEventManager();
        dem.eventReceived(genDeviceEvent("xbox", "success", "Teapot", "US", System.currentTimeMillis()));
        dem.eventReceived(genDeviceEvent("iphone", "failed", "Hightable", "CN", System.currentTimeMillis()));
        dem.eventReceived(genDeviceEvent("xbox", "success", "Pinetree", "UK", System.currentTimeMillis()));

        List<AggregatedEvent> events = dem.eventReport();
        assertEquals(events.size(), 2);
    }

    @Test
    void eventCategoryTest() {
        // Aggregated events are categorized in device, title and country.
        DeviceEventManager dem = new DeviceEventManager();
        dem.eventReceived(genDeviceEvent("xbox", "success", "Teapot", "US", System.currentTimeMillis()));
        dem.eventReceived(genDeviceEvent("xbox", "success", "Pinetree", "UK", System.currentTimeMillis()));
        dem.eventReceived(genDeviceEvent("irob", "success", "Pinetree", "UK", System.currentTimeMillis()));

        List<AggregatedEvent> events = dem.eventReport();
        assertEquals(events.size(), 3);
        assertEquals(events.get(0).toString(), "#AggregatedEvent{device:\"xbox\", sps:1, title:\"Teapot\", country:\"US\"}");
        assertEquals(events.get(1).toString(), "#AggregatedEvent{device:\"xbox\", sps:1, title:\"Pinetree\", country:\"UK\"}");
        assertEquals(events.get(2).toString(), "#AggregatedEvent{device:\"irob\", sps:1, title:\"Pinetree\", country:\"UK\"}");
    }
}