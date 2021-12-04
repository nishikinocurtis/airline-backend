package edu.curtis.airlinebackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Purchase {
    String ticketId, customerEmail, bookingAgentId;
    Date purchaseDate;
}
