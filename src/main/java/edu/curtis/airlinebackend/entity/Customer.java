package edu.curtis.airlinebackend.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.lang.String;
import java.lang.Integer;
import java.util.Date;


@Data
@NoArgsConstructor
public class Customer {
    String email, name, password, buildingNumber, street;
    String city, state, phoneNumber, passportNumber, passportCountry;
    @JsonFormat(pattern = "MM-dd-yyyy HH:mm:ss")
    @DateTimeFormat(pattern = "MM-dd-yyyy HH:mm:ss")
    Date passportExpiration, dateOfBirth;
}
