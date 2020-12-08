package com.covidnotifier.streamprocessing.model;

import com.fasterxml.jackson.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "userId",
        "uuid",
        "timestamp",
        "appUserPosition",
        "covidPositive"
})
public class AppUser {


    private String userId;

    private String uuid;

    private String timeStamp;


    private Map<String,String> appUserPosition;
    private String covidPositive;

    public AppUser(String userId, String uuid, String timeStamp) {
        this.userId = userId;
        this.uuid = uuid;
        this.timeStamp = timeStamp;

    }

    public AppUser(String userId, String uuid, Date timeStamp,  String covidPositive) {
        super();
        this.covidPositive = covidPositive;
    }


    public AppUser() {
    }

    @JsonProperty("userId")
    public String getUserId() {
        return userId;
    }
    @JsonProperty("userId")
    public void setUserId(String userId) {
        this.userId = userId;
    }
    @JsonProperty("timestamp")
    public String getTimeStamp() {
        return timeStamp;
    }
    @JsonProperty("timestamp")
    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
    @JsonProperty("uuid")
    public String getUuid() {
        return uuid;
    }
    @JsonProperty("uuid")
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    @JsonProperty("appUserPosition")
    public Map<String, String> getAppUserPosition() {
        return appUserPosition;
    }

    @JsonProperty("appUserPosition")
    public void setAppUserPosition(Map<String, String> appUserPosition) {
        this.appUserPosition = appUserPosition;
    }

    @JsonProperty("covidPositive")
    public String getCovidPositive() {
        return covidPositive;
    }
    @JsonProperty("covidPositive")
    public void setCovidPositive(String covidPositive) {
        this.covidPositive = covidPositive;
    }

    @Override
    public String toString() {
        return "AppUser{" +
                "userId='" + userId + '\'' +
                ", uuid='" + uuid + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", covidPositive='" + covidPositive + '\'' +
                '}';
    }
}


