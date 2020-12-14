package de.tu_berlin.mpds.covid_notifier.config;

import lombok.Data;
import lombok.ToString;



@Data
@ToString

public class Configuration {

    
    public static final String BOOTSTRAPSERVER ="bootstrap.servers";
    public static final String BOOTSTRAPSERVERADDRESS ="kafka:9092";

    public static final String THREADNAME= "group.id";
    public static final String THREADVALUE= "covidAnalyser";
    public static final String KAFKATOPIC="covid" ;

    public static final String HIGHRISKTOPIC="highrisk";

    public static final String INFECTIONSTOPIC="infections";
}
