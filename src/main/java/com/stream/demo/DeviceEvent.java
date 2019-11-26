package com.stream.demo;

public class DeviceEvent {
    private String device = null;
    private String sev = null;
    private String title = null;
    private String country = null;
    private long time = 0;

    public String getDevice() { return this.device; }
    public String getSev() { return this.sev; }
    public String getTitle() { return this.title; }
    public String getCountry() { return this.country; }
    public long getTime() { return this.time; }

    public void setDevice(String device) { this.device = device; }
    public void setSev(String sev) { this.sev = sev; }
    public void setTitle(String title) { this.title = title; }
    public void setCountry(String country) { this.country = country; }
    public void setTime(long time) { this.time = time; }

    @Override
    public String toString() {
        return String.format(
            "#DeviceEvent{device:\"%s\", sev:\"%s\", title:\"%s\", country:\"%s\", time:%d}",
            device, sev, title, country, time);
    }
}