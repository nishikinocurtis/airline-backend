package edu.curtis.airlinebackend.controller;

import edu.curtis.airlinebackend.entity.*;
import edu.curtis.airlinebackend.service.MyBatisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/agent")
public class AgentController {
    @Autowired
    private MyBatisService myBatisService;


    @GetMapping
    public String init() {
        return "API init";
    }

    @PostMapping("/view-my-flights")
    public List<Record> viewMyFlights(@RequestBody RequestAgent requestAgent) {
        return myBatisService.viewTicketsByAgentId(requestAgent.getBookingAgentId());
    }

    @PostMapping("/view-commission")
    public Double viewMyCommission(@RequestBody RequestAgent requestAgent) {
        return myBatisService.getCommissionById(requestAgent.getBookingAgentId(),
                requestAgent.getDateFrom(),
                requestAgent.getDateTo());
    }

    @PostMapping("/view-customers-number")
    public List<KVData> viewCustomerRankByNumber(@RequestBody RequestAgent requestAgent) {
        return myBatisService.getCustomerRankByTicketNumber(requestAgent.getBookingAgentId(),
                requestAgent.getDateFrom(),
                requestAgent.getDateTo());
    }

    @PostMapping("/view-customers-commission")
    public List<KVData> viewCustomerRankByCommission(@RequestBody RequestAgent requestAgent) {
        return myBatisService.getCustomerRankByTicketPrice(requestAgent.getBookingAgentId(),
                requestAgent.getDateFrom(),
                requestAgent.getDateTo());
    }

    @PostMapping("/verification")
    public Boolean loginVerification(@RequestBody LoginRequest loginRequest) {
        List<String> result = myBatisService.loginVerification(
                loginRequest.getVid(),
                loginRequest.getVpassword(), "agent");
        return !result.isEmpty();
    }


}
