package com.covidnotifier.streamprocessing.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AppUserPosition {

    private long longi;
    private long lati;

    public AppUserPosition(long longitude, long latitude) {
        this.longi = longitude;
        this.lati = latitude;
    }
    @JsonProperty("longi")
    public long getLongi() {
        return longi;
    }
    @JsonProperty("longi")
    public void setLongi(long longi) {
        this.longi = longi;
    }
    @JsonProperty("lati")
    public long getLati() {
        return lati;
    }
    @JsonProperty("lati")
    public void setLati(long lati) {
        this.lati = lati;
    }

    @Override
    public String toString() {
        return
                getLongi()+ " " + getLati() ;
    }
}
