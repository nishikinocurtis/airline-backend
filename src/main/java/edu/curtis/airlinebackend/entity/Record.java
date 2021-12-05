package edu.curtis.airlinebackend.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class Record {
    String ticketId, email, airlineName, flightNum, deptPort, arriPort, status;
    Date deptTime, arriTime;
    Double price;
}
