package edu.curtis.airlinebackend.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
public class BookingAgent {
    String email, password, bookingAgentId;
}