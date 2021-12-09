package edu.curtis.airlinebackend.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Purchase {
    String ticketId, customerEmail, bookingAgentId;
    @JsonFormat(pattern = "MM-dd-yyyy HH:mm:ss")
    @DateTimeFormat(pattern = "MM-dd-yyyy HH:mm:ss")
    Date purchaseDate;
}
