package edu.curtis.airlinebackend.controller;

import edu.curtis.airlinebackend.entity.*;
import edu.curtis.airlinebackend.service.MyBatisService;
import edu.curtis.airlinebackend.utility.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping
public class PublicController {
    @Autowired
    private MyBatisService myBatisService;
    @Autowired
    private PlatformTransactionManager transactionManager;


    @GetMapping
    public String init() {
        return "API init";
    }

    @GetMapping("/search-airport")
    public List<Airport> searchAirport(String nameString) {
        return myBatisService.searchAirport(nameString);
    }

    @PostMapping("/search-flights")
    public List<Flight> searchFlights(@RequestBody RequestFlight requestFlight) {
        return myBatisService.searchFlight(requestFlight);
    }

    @PostMapping("/create-order")
    public String purchaseTicketForUser(@RequestBody RequestOrder requestOrder) {
        TransactionStatus txStatus =
                transactionManager.getTransaction(new DefaultTransactionDefinition());
        String ticketId = Util.genTicketId();

        //insert ticket record and return ticket
        Ticket t = new Ticket(ticketId, requestOrder.getAirlineName(), requestOrder.getFlightNum());
        Purchase p = new Purchase(ticketId,
                requestOrder.getCustomerEmail(),
                requestOrder.getBookingAgentId(),
                new Date());
        try {
            myBatisService.createTicket(t);
            myBatisService.createPurchase(p);
        } catch (Exception e) {
            transactionManager.rollback(txStatus);
            return "Error: Creating Order Failed.";
        }
        transactionManager.commit(txStatus);

        return ticketId;
    }
}
