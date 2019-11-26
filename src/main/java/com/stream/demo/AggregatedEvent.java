package com.stream.demo;

public class AggregatedEvent {
    private String device = null;
    private long sps = 0;
    private String title = null;
    private String country = null;

    public String getDevice() { return this.device; }
    public long getSps() { return this.sps; }
    public String getTitle() { return this.title; }
    public String getCountry() { return this.country; }

    public void setDevice(String device) { this.device = device; }
    public void setSps(long sps) { this.sps = sps; }
    public void setTitle(String title) { this.title = title; }
    public void setCountry(String country) { this.country = country; }

    public AggregatedEvent(String device, long sps, String title, String country) {
        this.device = device;
        this.sps = sps;
        this.title = title;
        this.country = country;
    }

    @Override
    public String toString() {
        return String.format(
            "#AggregatedEvent{device:\"%s\", sps:%d, title:\"%s\", country:\"%s\"}",
            device, sps, title, country);
    }
}