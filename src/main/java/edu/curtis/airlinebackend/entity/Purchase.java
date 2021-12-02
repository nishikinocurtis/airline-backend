package edu.curtis.airlinebackend.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
public class Purchase {
    String ticketId, customerEmail, bookingAgentId;
    Date purchaseDate;
}
