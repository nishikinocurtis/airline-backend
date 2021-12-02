package edu.curtis.airlinebackend.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Record {
    Purchase purchase;
    Customer customer;
    BookingAgent bookingAgent;
    Flight flight;
}
