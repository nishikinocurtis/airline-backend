package edu.curtis.airlinebackend.controller;

import edu.curtis.airlinebackend.entity.*;
import edu.curtis.airlinebackend.service.MyBatisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/staff")
public class StaffController {
    @Autowired
    private MyBatisService myBatisService;

    @PostMapping("/verification")
    public String loginVerification(@RequestBody LoginRequest loginRequest) {
        List<String> tmp = myBatisService.staffLoginVerification(loginRequest.getVid(), loginRequest.getVpassword());
        if (tmp.isEmpty()) return "";
        else return tmp.get(0);
    }

    @PostMapping("/get-permission")
    public List<String> checkPermission(@RequestBody String username) {
        List<String> tmp = myBatisService.getAirlineByUsername(username);
        if (!tmp.isEmpty()) {
            tmp.addAll(myBatisService.checkPermission(username));
            return tmp;
        }
        return new ArrayList<>();
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

    @PostMapping("/create-airport")
    public Boolean createAirport(@RequestBody Airport a) {
        return myBatisService.createAirport(a);
    }

    // statistics
    @PostMapping("/view-customers")
    public List<KVData> viewFrequentCustomer(@RequestBody ListKVDateData query) {
        List<KVData> result = new ArrayList<>();
        for (DateRange dateRange : query.getData()) {
            List<KVData> segment = myBatisService.getFrequentCustomer(query.getKey(), dateRange);
            for (KVData customer : segment) {
                result.add(customer);
            }
        }
        return result;
    }

    // TODO: to be modified
    @PostMapping("/view-customers/get-flights")
    public List<Record> viewCustomerGetFlights(@RequestBody StringPair stringPair) {
        return myBatisService.getCustomerAllFlights(stringPair.getKey(), stringPair.getValue());
        // key = email, value = airline name
    }

    @PostMapping("/view-agents/number")
    public List<KVData> viewTopAgentsByNumber(@RequestBody ListKVDateData query) {
        List<KVData> result = new ArrayList<>();
        for (DateRange dateRange : query.getData()) {
            List<KVData> segment = myBatisService.getAgentRankByTicketNumber(query.getKey(), dateRange);
            result.addAll(segment);
        }
        return result;
    }

    @PostMapping("/view-agents/sales")
    public List<KVData> viewTopAgentsBySales(@RequestBody ListKVDateData query) {
        List<KVData> result = new ArrayList<>();
        for (DateRange dateRange : query.getData()) {
            List<KVData> segment = myBatisService.getAgentRankByTotalPayments(query.getKey(), dateRange);
            result.addAll(segment);
        }
        return result;
    }

    @PostMapping("/reports")
    public List<DateRange> viewReports(@RequestBody ListKVDateData query) {
        List<DateRange> result = new ArrayList<>();
        for (DateRange dateRange : query.getData()) {
            Integer number = myBatisService.getTotalSales(query.getKey(), dateRange.getDateFrom(), dateRange.getDateTo());
            result.add(new DateRange(dateRange.getDateFrom(), dateRange.getDateTo(), number.doubleValue()));
        }
        // need to be summed up by front-end.
        return result;
    }

    @PostMapping("/comparison")
    public Map<String, Double> viewComparison(@RequestBody ListKVDateData query) {
        Map<String, Double> result = new HashMap<>();
        for (DateRange dateRange : query.getData()) {
            return myBatisService.getComparison(query.getKey(), dateRange);
        }
        return result;
    }

    @PostMapping("/destinations")
    public Map<String, List<KVData>> viewTopDestinations(@RequestBody ListKVDateData query) {
        Map<String, List<KVData>> result = new HashMap<>();
        Integer counter = 0;
        for (DateRange dateRange : query.getData()) {
            System.out.println(counter);
            result.put(counter.toString(), myBatisService.getTopDestinations(dateRange));
            counter++;
        }
        return result;
    }

    @PostMapping("/grant-permissions")
    public Boolean grantNewPermission(@RequestBody StringPair permission) {
        return myBatisService.grantPermission(permission);
    }

    @PostMapping("/add-agent")
    public Boolean addNewAgent(@RequestBody StringPair agentInfo) { //admin only
        BookingAgentAffiliation af = new BookingAgentAffiliation(agentInfo.getKey(), agentInfo.getValue());
        return myBatisService.addNewAgent(af);
    }
}
