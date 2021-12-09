package edu.curtis.airlinebackend.service;

import edu.curtis.airlinebackend.entity.*;
import edu.curtis.airlinebackend.mapper.MyBatisMapper;
import edu.curtis.airlinebackend.utility.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.*;

@Controller
public class MyBatisService {
    @Autowired
    private MyBatisMapper myBatisMapper;
    @Autowired
    private PlatformTransactionManager transactionManager;
    /*
    public List<Ticket> getTicketsByUser(String email) {

    }
    */
    public List<Airport> searchAirport(String nameString) {
        return myBatisMapper.getAirport("%" + nameString + "%");
    }

    public List<Flight> searchFlight(RequestFlight requestFlight) {
        System.out.println(requestFlight.toString());
        try {
            List<Flight> result = myBatisMapper.getFlight( requestFlight.getDeparturePort(),
                    requestFlight.getArrivalPort(),
                    requestFlight.getDateFrom(),
                    requestFlight.getDateTo());
            return result;
        } catch (Exception e) {
            System.out.println(e);
            return new ArrayList<>();
        }

    }

    public String createOrder(Purchase p, Ticket t) { // notice agent affiliation
        TransactionStatus txStatus =
                transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            if (!p.getBookingAgentId().equals("")) {
                if (myBatisMapper.affiliationValidation(p.getBookingAgentId(), t.getAirlineName()).isEmpty()) {
                    transactionManager.rollback(txStatus);
                    return "Error: Affiliation not exists.";
                }
            }
            myBatisMapper.insertTickets(t);
            myBatisMapper.insertPurchase(p);
        } catch (Exception e) {
            transactionManager.rollback(txStatus);
            return e.getMessage();
        }
        transactionManager.commit(txStatus);
        return t.getTicketId();
    }

    public String customerRegistration(String email, String name, String password, String building_number, String street, String city,
                                       String state, String phone_number, String passport_number, Date passport_exp, String passport_country,
                                       Date DOB) { // notice agent affiliation
        TransactionStatus txStatus =
                transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            myBatisMapper.customerRegistration(email, name, password, building_number, street, city, state,
                    phone_number, passport_number, passport_exp, passport_country, DOB);
        } catch (Exception e) {
            transactionManager.rollback(txStatus);
            return "Error: Creating Customer Failed.";
        }
        transactionManager.commit(txStatus);
        return email;
    }

    public List<Record> viewTicketsByEmail(String email) {
        System.out.println(email);
        try {
            return myBatisMapper.getTicketsByEmail(email);
        } catch (Exception e) {
            System.out.println(e);
            return new ArrayList<>();
        }
    }

    public List<Record> viewTicketsByAgentId(String agentId) {
        return myBatisMapper.getTicketsById(agentId);
    }

    public Double getCommissionById(String Id, Date dateFrom, Date dateTo) {
        return myBatisMapper.getTotalSellsByIdAndDate(Id, dateFrom, dateTo);
    }

    public List<KVData> getCustomerRankByTicketNumber(String Id, Date dateFrom, Date dateTo) {
        return myBatisMapper.getCustomerRankByTicketNumber(Id, dateFrom, dateTo);
    }

    public List<KVData> getCustomerRankByTicketPrice(String Id, Date dateFrom, Date dateTo) {
        return myBatisMapper.getCustomerRankByTicketPrice(Id, dateFrom, dateTo);
    }

    public List<String> loginVerification(String Id, String password, String role) {
        if (role.equals("agent")) return myBatisMapper.agentLoginVerification(Id, password);
        else return myBatisMapper.loginVerification(Id, password, role);
    }

    public List<String> staffLoginVerification(String username, String password) {
        return myBatisMapper.staffLoginVerification(username, password);
    }

    public List<String> checkPermission(String username) {
        return myBatisMapper.getPermissionList(username);
    }

    public Double getTotalPaymentsByDate(String email, Date dateFrom, Date dateTo) {
        return myBatisMapper.getTotalPaymentsByEmailAndDate(email, dateFrom, dateTo);
    }

    public List<Flight> getFlightsByDate(String airline, Date dateFrom, Date dateTo) {
        return myBatisMapper.viewFlightsByDate(airline, dateFrom, dateTo);
    }

    public List<String> getCustomersByFlight(String flight_num) {
        return myBatisMapper.viewCustomersByFlight(flight_num);
    }

    public Boolean createFlight(Flight f) {
        TransactionStatus txStatus =
                transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            myBatisMapper.createFlight(f);
        } catch (Exception e) {
            System.out.println(e);
            transactionManager.rollback(txStatus);
            return Boolean.FALSE;
        }
        transactionManager.commit(txStatus);
        return Boolean.TRUE;
    }

    public Boolean flipFlightStatus(Flight flight) {
        TransactionStatus txStatus =
                transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            myBatisMapper.flipFlightStatus(flight.getAirlineName(), flight.getFlightNum(), flight.getDepartureTime(), flight.getStatus());
        } catch (Exception e) {
            System.out.println(e);
            transactionManager.rollback(txStatus);
            return Boolean.FALSE;
        }
        transactionManager.commit(txStatus);
        return Boolean.TRUE;
    }

    public Boolean createAirplane(Airplane a) {
        TransactionStatus txStatus =
                transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            myBatisMapper.createAirplane(a);
        } catch (Exception e) {
            System.out.println(e);
            transactionManager.rollback(txStatus);
            return Boolean.FALSE;
        }
        transactionManager.commit(txStatus);
        return Boolean.TRUE;
    }

    public Boolean createAirport(Airport a) {
        TransactionStatus txStatus =
                transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            myBatisMapper.createAirport(a);
        } catch (Exception e) {
            System.out.println(e);
            transactionManager.rollback(txStatus);
            return Boolean.FALSE;
        }
        transactionManager.commit(txStatus);
        return Boolean.TRUE;
    }

    public Boolean grantPermission(StringPair sp) {
        TransactionStatus txStatus =
                transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            myBatisMapper.grantPermission(sp.getKey(), sp.getValue());
        } catch (Exception e) {
            transactionManager.rollback(txStatus);
            return Boolean.FALSE;
        }
        transactionManager.commit(txStatus);
        return Boolean.TRUE;
    }

    public Boolean createNewAgent(BookingAgent agent) {
        TransactionStatus txStatus =
                transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            myBatisMapper.insertBookingAgent(agent);
        } catch (Exception e) {
            System.out.println(e);
            transactionManager.rollback(txStatus);
            return Boolean.FALSE;
        }
        transactionManager.commit(txStatus);
        return Boolean.TRUE;
    }

    public Boolean addNewAgent(BookingAgentAffiliation af) {
        TransactionStatus txStatus =
                transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            myBatisMapper.addAgentToAirline(af);
        } catch (Exception e) {
            transactionManager.rollback(txStatus);
            return Boolean.FALSE;
        }
        transactionManager.commit(txStatus);
        return Boolean.TRUE;
    }

    public List<KVData> getFrequentCustomer(String airlineName, DateRange dateRange) {
        return myBatisMapper.getCustomerRankInAirline(airlineName, dateRange.getDateFrom(), dateRange.getDateTo(), dateRange.getValue().intValue());
    }

    public List<Record> getCustomerAllFlights(String email, String airlineName) {
        return myBatisMapper.getTicketsByEmailInAirline(email, airlineName);
    }

    public List<KVData> getAgentRankByTicketNumber(String airlineName, DateRange dateRange) {
        return myBatisMapper.getAgentRankByTicketNumber(airlineName, dateRange.getDateFrom(), dateRange.getDateTo(), dateRange.getValue().intValue());
    }

    public List<KVData> getAgentRankByTotalPayments(String airlineName, DateRange dateRange) {
        return myBatisMapper.getAgentRankByTotalPayments(airlineName, dateRange.getDateFrom(), dateRange.getDateTo(), dateRange.getValue().intValue());
    }

    public Integer getTotalSales(String airlineName, Date dateFrom, Date dateTo) {
        return myBatisMapper.getTotalSales(airlineName, dateFrom, dateTo);
    }

    public Map<String, Double> getComparison(String airline, DateRange dateRange) {
        List<KVData> agentSum = myBatisMapper.getAgentRankByTotalPayments(airline, dateRange.getDateFrom(), dateRange.getDateTo(), 999);
        Double nonAgent = 0.0;
        Double agent = 0.0;
        for (KVData item : agentSum) {
            if (item.getKey().equals("")) {
                nonAgent = item.getValue();
            } else {
                agent += item.getValue();
            }
        }

        Map<String, Double> result = new HashMap<>();
        result.put("agent", agent);
        result.put("nonAgent", nonAgent);
        return result;
    }

    public List<KVData> getTopDestinations(DateRange dateRange) {
        return myBatisMapper.getDestinations(dateRange.getDateFrom(), dateRange.getDateTo(), dateRange.getValue().intValue());
    }

    public List<String> getAirlineByUsername(String username) {
        return myBatisMapper.getAirline(username);
    }
}
