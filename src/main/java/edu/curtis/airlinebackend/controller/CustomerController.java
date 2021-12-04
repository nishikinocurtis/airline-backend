package edu.curtis.airlinebackend.controller;

import edu.curtis.airlinebackend.entity.*;
import edu.curtis.airlinebackend.service.MyBatisService;
import edu.curtis.airlinebackend.utility.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    MyBatisService myBatisService;

    @GetMapping
    public String init() { return "API init"; }

    @PostMapping("/verification")
    public Boolean loginVerification(@RequestBody LoginRequest loginRequest) {
        List<String> result = myBatisService.loginVerification(
                loginRequest.getVId(),
                loginRequest.getVPassword(), "customer");
        return !result.isEmpty();
    }

    // TODO: to be modified
    @PostMapping("/view-my-flights")
    public List<Record> viewMyFlights(@RequestBody String email) {
        List<Record> result = new ArrayList<>();
        return result;
    }

    @PostMapping("/track-spending")
    public List<DateRange> trackSpending(@RequestBody ListKVDateData query) {
        List<DateRange> result = new ArrayList<>();
        for (DateRange range : query.getData()) {
            double rangeResult = myBatisService.getTotalPaymentsByDate(query.getKey(), range.getDateFrom(), range.getDateTo());
            DateRange item = new DateRange(range.getDateFrom(), range.getDateTo(), rangeResult);
            result.add(item);
        }
        return result;
    }
}
