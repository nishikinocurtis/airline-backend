package edu.curtis.airlinebackend.controller;

import edu.curtis.airlinebackend.entity.*;
import edu.curtis.airlinebackend.service.MyBatisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/staff")
public class StaffController {
    @Autowired
    private MyBatisService myBatisService;

    @PostMapping("/verification")
    public Boolean loginVerification(@RequestBody LoginRequest loginRequest) {
        return !myBatisService.staffLoginVerification(loginRequest.getVId(), loginRequest.getVPassword()).isEmpty();
    }

    @PostMapping("/view-flights")
    public List<Flight> viewAirlineFlights(@RequestBody RequestAgent requestAirline) {
        return myBatisService.getFlightsByDate(requestAirline.getBookingAgentId(),
                requestAirline.getDateFrom(), requestAirline.getDateTo());
    }

    @PostMapping("/view-flights/get-customers")
    public List<String> viewCustomersByFlight(@RequestBody String flightNum) {
        return myBatisService.getCustomersByFlight(flightNum);
    }

    @PostMapping("/create-flight")
    public Boolean createFlight(@RequestBody Flight flight) {
        return myBatisService.createFlight(flight);
    }

    @PostMapping("/change-flight")
    public Boolean changeFlightStatus(@RequestBody Flight flight) {
        return myBatisService.flipFlightStatus(flight);
    }

    @PostMapping("/create-airplane")
    public Boolean createAirplane(@RequestBody Airplane a) {
        return myBatisService.createAirplane(a);
    }
}
