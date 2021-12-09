package edu.curtis.airlinebackend.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
public class Record {
    String ticketId, email, airlineName, flightNum, deptPort, arriPort, status;
    @JsonFormat(pattern = "MM-dd-yyyy HH:mm:ss")
    @DateTimeFormat(pattern = "MM-dd-yyyy HH:mm:ss")
    Date deptTime, arriTime;
    Double price;
}
