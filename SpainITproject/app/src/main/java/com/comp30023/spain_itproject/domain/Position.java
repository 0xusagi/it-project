package com.comp30023.spain_itproject.domain;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Position {

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    private float lat;
    private float lng;
    private String timeStamp;

    public String getDestinationID() {
        return destinationID;
    }

    public void setDestinationID(String destinationID) {
        this.destinationID = destinationID;
    }

    private String destinationID;

    public Position() {

    }

    public Position(float lat, float lng, String destinationID) {

        this.lat = lat;
        this.lng = lng;
        this.destinationID = destinationID;

        TimeZone tz = TimeZone.getDefault();
        DateFormat df = SimpleDateFormat.getDateTimeInstance();
        df.setTimeZone(tz);
        this.timeStamp = df.format(new Date());
    }
}
