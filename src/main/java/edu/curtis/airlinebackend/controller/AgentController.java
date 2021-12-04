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
@RequestMapping("/agent")
public class AgentController {
    @Autowired
    private MyBatisService myBatisService;
    @Autowired
    private PlatformTransactionManager transactionManager;


    @GetMapping
    public String init() {
        return "API init";
    }

    //TODO: To be modified
    @PostMapping("/view-my-flights")
    public List<Record> viewMyFlights(@RequestBody RequestAgent requestAgent) {
        List<Record> result = new ArrayList<>();
        return result;
    }

    @PostMapping("/view-commission")
    public double viewMyCommission(@RequestBody RequestAgent requestAgent) {
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
                loginRequest.getVId(),
                loginRequest.getVPassword(), "agent");
        return !result.isEmpty();
    }


}
