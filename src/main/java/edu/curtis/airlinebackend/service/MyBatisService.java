package edu.curtis.airlinebackend.service;

import edu.curtis.airlinebackend.entity.*;
import edu.curtis.airlinebackend.mapper.MyBatisMapper;
import edu.curtis.airlinebackend.utility.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
        return myBatisMapper.getFlight( requestFlight.getDeparturePort(),
                                        requestFlight.getArrivalPort(),
                                        requestFlight.getDateTimeFrom(),
                                        requestFlight.getDateTimeTo());
    }

    public String createOrder(Purchase p, Ticket t) {
        TransactionStatus txStatus =
                transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            myBatisMapper.insertTickets(t);
            myBatisMapper.insertPurchase(p);
        } catch (Exception e) {
            transactionManager.rollback(txStatus);
            return "Error: Creating Order Failed.";
        }
        transactionManager.commit(txStatus);
        return t.getTicketId();
    }

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

    public List<String> staffLoginVerification(String username, String password) {
        return myBatisMapper.staffLoginVerification(username, password);
    }

    public double getTotalPaymentsByDate(String email, Date dateFrom, Date dateTo) {
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
            transactionManager.rollback(txStatus);
            return Boolean.FALSE;
        }
        transactionManager.commit(txStatus);
        return Boolean.TRUE;
    }
}
