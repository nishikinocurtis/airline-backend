package edu.curtis.airlinebackend.controller;

import edu.curtis.airlinebackend.entity.Airport;
import edu.curtis.airlinebackend.entity.Ticket;
import edu.curtis.airlinebackend.service.MyBatisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/agent")
public class AgentController {
    @Autowired
    private MyBatisService myBatisService;

    @GetMapping
    public String init() {
        return "API init";
    }

    @GetMapping("/search-airport")
    public List<Airport> searchAirport(String nameString) {
        return myBatisService.searchAirport(nameString);
    }
}
