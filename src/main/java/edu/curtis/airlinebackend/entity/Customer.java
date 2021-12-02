package edu.curtis.airlinebackend.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.lang.String;
import java.lang.Integer;
import java.util.Date;


@Data
@NoArgsConstructor
public class Customer {
    String email, name, password, buildingNumber, street;
    String city, state, passportNumber, passportCountry;
    Date passportExpiration, dateOfBirth;
    Integer phone_number;
}
