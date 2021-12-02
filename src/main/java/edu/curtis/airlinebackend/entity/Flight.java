package edu.curtis.airlinebackend.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
public class Flight {
    String airlineName, flightNum, departurePort,  arrivalPort, status, airplaneId;
    Date departureTime, arrivalTime;
    double price;
}
