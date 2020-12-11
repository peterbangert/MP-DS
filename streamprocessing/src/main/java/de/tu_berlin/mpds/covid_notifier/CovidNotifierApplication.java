package de.tu_berlin.mpds.covid_notifier;

import org.apache.log4j.BasicConfigurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CovidNotifierApplication {

	public static void main(String[] args) {
		//BasicConfigurator.configure();
		SpringApplication.run(CovidNotifierApplication.class, args);
	}

}
