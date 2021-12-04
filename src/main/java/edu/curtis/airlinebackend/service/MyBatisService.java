package edu.curtis.airlinebackend.service;

import edu.curtis.airlinebackend.entity.*;
import edu.curtis.airlinebackend.mapper.MyBatisMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class MyBatisService {
    @Autowired
    private MyBatisMapper myBatisMapper;
    /*
    public List<Ticket> getTicketsByUser(String email) {

    }
    */
    public List<Airport> searchAirport(String nameString) {
        return myBatisMapper.getAirport("%" + nameString + "%");
    }

    public List<Flight> searchFlight(RequestFlight requestFlight) {
        return myBatisMapper.getFlight( requestFlight.getDeparturePort(),
                                        requestFlight.getArrivalPort(),
                                        requestFlight.getDateTimeFrom(),
                                        requestFlight.getDateTimeTo());
    }

    public void createPurchase(Purchase p) { myBatisMapper.insertPurchase(p); }

    public void createTicket(Ticket t) { myBatisMapper.insertTickets(t); }

    public double getCommissionById(String Id, Date dateFrom, Date dateTo) {
        return myBatisMapper.getTotalSellsByIdAndDate(Id, dateFrom, dateTo);
    }

    public List<KVData> getCustomerRankByTicketNumber(String Id, Date dateFrom, Date dateTo) {
        return myBatisMapper.getCustomerRankByTicketNumber(Id, dateFrom, dateTo);
    }

    public List<KVData> getCustomerRankByTicketPrice(String Id, Date dateFrom, Date dateTo) {
        return myBatisMapper.getCustomerRankByTicketPrice(Id, dateFrom, dateTo);
    }

    public List<String> loginVerification(String Id, String password, String role) {
        return myBatisMapper.loginVerification(Id, password, role);
    }

    public double getTotalPaymentsByDate(String email, Date dateFrom, Date dateTo) {
        return myBatisMapper.getTotalPaymentsByEmailAndDate(email, dateFrom, dateTo);
    }
}
