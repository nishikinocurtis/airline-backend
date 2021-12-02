package edu.curtis.airlinebackend.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
public class Staff {
    String username, password, fName, lName, airlineName;
    Date dateOfBirth;
}
