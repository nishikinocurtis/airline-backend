package edu.curtis.airlinebackend.entity;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class Airplane {
    String airlineName, airplaneId;
    Integer seats;
}
