package edu.curtis.airlinebackend.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


@Data
@NoArgsConstructor
public class Flight {
    String airlineName, flightNum, departurePort,  arrivalPort, status, airplaneId;
    @JsonFormat(pattern = "MM-dd-yyyy HH:mm:ss")
    @DateTimeFormat(pattern = "MM-dd-yyyy HH:mm:ss")
    Date departureTime, arrivalTime;
    double price;
}
