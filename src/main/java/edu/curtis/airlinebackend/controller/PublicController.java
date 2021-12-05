package edu.curtis.airlinebackend.controller;

import edu.curtis.airlinebackend.entity.*;
import edu.curtis.airlinebackend.service.MyBatisService;
import edu.curtis.airlinebackend.utility.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping
public class PublicController {
    @Autowired
    private MyBatisService myBatisService;


    @GetMapping
    public String init() {
        return "API init";
    }

    @GetMapping("/search-airport")
    public List<Airport> searchAirport(@RequestParam("name") String nameString) {
        return myBatisService.searchAirport(nameString);
    }

    @PostMapping("/search-flights")
    public List<Flight> searchFlights(@RequestBody RequestFlight requestFlight) {
        return myBatisService.searchFlight(requestFlight);
    }

    @PostMapping("/create-order")
    public String purchaseTicketForUser(@RequestBody RequestOrder requestOrder) {
        String ticketId = Util.genTicketId();

        //insert ticket record and return ticket
        Ticket t = new Ticket(ticketId, requestOrder.getAirlineName(), requestOrder.getFlightNum());
        Purchase p = new Purchase(ticketId,
                requestOrder.getCustomerEmail(),
                requestOrder.getBookingAgentId(),
                new Date());

        return myBatisService.createOrder(p, t);
    }

    @PostMapping("/create-agent")
    public String createNewAgent(@RequestBody LoginRequest agentInfo) { //registration
        String bookingAgentId = Util.genUUID();
        BookingAgent agent = new BookingAgent(agentInfo.getVId(), agentInfo.getVPassword(), bookingAgentId);
        if (myBatisService.createNewAgent(agent)) {
            return bookingAgentId;
        } else {
            return "Error: Creating Agent Failed.";
        }
    }
}
