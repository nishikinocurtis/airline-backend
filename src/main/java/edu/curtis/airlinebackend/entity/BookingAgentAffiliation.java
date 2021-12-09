package edu.curtis.airlinebackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingAgentAffiliation {
    String email, airlineName; // all emails are now id.
}
