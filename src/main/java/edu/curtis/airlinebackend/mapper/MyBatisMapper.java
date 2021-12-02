package edu.curtis.airlinebackend.mapper;

import edu.curtis.airlinebackend.entity.*;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface MyBatisMapper {
    // Public Part
    @Insert("INSERT INTO ticket VALUES (#{t.ticketId}, #{t.airlineName), #{t.flightNum}")
    void insertTickets(Ticket t);

    @Insert("INSERT INTO purchases VALUES (#{p.ticketId}, #{p.customerEmail}, #{p.bookingAgentId}, #{p.purchaseDate})")
    void insertPurchase(Purchase p);

    @Select("SELECT * FROM flight f " +
            "WHERE departure_airport = #{deptPort} AND arrival_airport = #{arriPort} AND deptDate = #{date}")
    List<Flight> getFlightByDeptArriDate(String deptPort, String arriPort, Date date);

    //TODO: Deal with pattern %% in Service.
    @Select("SELECT * FROM airport ap " +
            "WHERE ap.name LIKE #{search} OR ap.city LIKE #{search}")
    List<Airport> getAirport(String search);

    @Select("SELECT * FROM flight f " +
            "WHERE f.departure_airport = #{deptPort} AND f.arrival_airport = #{arriPort} AND f.departure_time > #{dateFrom} AND f.departure_time < #{dateTime}")
    List<Flight> getFlight(String deptPort, String arriPort, Date dateFrom, Date dateTo);

    // Customer Part
    @Select("SELECT * FROM purchases p WHERE purchases.customer_email = #{email}" +
            "JOIN customer c ON purchase.customer_email = customer.email" +
            "JOIN ticket t ON ticket.ticket_id = purchase.ticket_id" +
            "JOIN flight f ON ticket.flight_num = flight.flight_num")
    List<Record> getTicketsByEmail(String email);
    //TODO: Modify

    @Select("SELECT SUM(f.price) FROM purchase p" +
            "WHERE p.customer_email = #{email} AND p.purchase_date >= #{dateFrom} AND p.purchase_date <= #{dateTo}" +
            "JOIN ticket t ON t.ticket_id = p.ticket_id" +
            "JOIN flight f ON f.flight_num = t.flight_num")
    double getTotalPaymentsByEmailAndDate(String email, Date dateFrom, Date dateTo);

    // Agent Part
    @Select("SELECT * FROM purchases p WHERE purchases.booking_agent_id = #{agentId}" +
            "JOIN customer c ON purchase.customer_email = customer.email" +
            "JOIN ticket t ON ticket.ticket_id = purchase.ticket_id" +
            "JOIN flight f ON ticket.flight_num = flight.flight_num")
    List<Record> getTicketsById(String agentId);
    //TODO: modify

    @Select("SELECT SUM(f.price) FROM purchase p " +
            "WHERE p.booking_agent_id = #{agentId} AND p.purchase_date >= #{dateFrom} AND p.purchase_date <= #{dateTo} " +
            "JOIN ticket t ON t.ticket_id = p.ticket_id " +
            "JOIN flight f ON t.flight_num = f.flight_num")
    double getTotalSellsByEmailAndDate(String agentId, Date dateFrom, Date dateTo);

    @Select("SELECT c.email, COUNT(*) FROM purchase p" +
            "WHERE p.booking_agent_id = #{agentId} AND p.purchase_date >= #{dateFrom} AND p.purchase_date <= #{dateTo}" +
            "JOIN ticket t ON t.ticket_id = p.ticket_id" +
            "JOIN flight f ON t.flight_num = f.flight_num" +
            "GROUP BY c.email")
    List<Map<String, Integer>> getCustomerRankByTicketNumber(String agentId, Date dateFrom, Date dateTo);

    @Select("SELECT c.email, SUM(f.price) FROM purchase p " +
            "WHERE p.booking_agent_id = #{agentId} AND p.purchase_date >= #{dateFrom} AND p.purchase_date <= #{dateTo} " +
            "JOIN ticket t ON t.ticket_id = p.ticket_id " +
            "JOIN flight f ON t.flight_num = f.flight_num " +
            "GROUP BY c.email")
    List<Map<String, Double>> getCustomerRankByTicketPrice(String agentId, Date dateFrom, Date dateTo);

    // Staff Part
    @Select("SELECT * FROM flight WHERE airline_name = #{airlineName}")
    List<Flight> viewFlights(String airlineName);

    @Insert("INSERT flight VALUES (#{f.airlineName}, #{f.flightNum}, #{f.departurePort}, " +
            "#{f.arrivalPort}, #{f.status}, #{f.airplaneId}, #{departureTime}, #{arrivalTime}, #{price})")
    void createFlight(Flight f);

    @Update("UPDATE flight SET status = #{newStatus} WHERE airline_name = {airlineName} AND " +
            "flight_num = #{flightName} AND departure_time = #{deptTime}")
    void flipFlightStatus(String airlineName, String flightNum, Date deptTime, String newStatus);

    @Insert("INSERT INTO airplane VALUES (#{a.airlineName}, #{a.airplaneId}, #{a.seats})")
    void createAirplane(Airplane a);

    @Insert("INSERT INTO airport VALUES (#{a.name}, #{a.city})")
    void createAirport(Airport a);

    @Insert("INSERT INTO booking_agent VALUES (#{b.email}, #{b.password}, #{b.bookingAgentId})")
    void createBookingAgent(BookingAgent b);

    @Insert("INSERT INTO booking_agent_work_for VALUES (#{af.email}, #{af.airlineName})")
    void addAgentToAirline(BookingAgentAffiliation af);

    @Insert("INSERT INTO permission VALUES (#{username}, #{prev})")
    void grantPermission(String username, String prev); // Staff username and privilege.

    @Select("SELECT permission_type pt FROM permission WHERE username = #{username}")
    @ResultType(java.lang.String.class)
    List<String> getPermissionList(String username);

    // Statistics Part
    @Select("SELECT p.booking_agent_id, COUNT(*) FROM purchases p " +
            "JOIN ticket t ON p.ticket_id = t.ticket_id " +
            "WHERE t.airline_name = #{airlineName} AND p.purchase_date > #{dateFrom} AND p.purchase_date < #{dateTo} " +
            "GROUP BY p.booking_agent_id")
    List<Map<String, Integer>> getAgentRankByTicketNumber(String airlineName, Date dateFrom, Date dateTo);

    @Select("SELECT p.booking_agent_id, SUM(f.price) FROM purchases p " +
            "JOIN ticket t ON p.ticket_id = t.ticket_id " +
            "JOIN flight f ON f.airline_num = t.flight_num " +
            "WHERE t.airline_name = #{airlineName} AND p.purchase_date > #{dateFrom} AND p.purchase_date < #{dateTo} " +
            "GROUP BY p.booking_agent_id")
    List<Map<String, Double>> getAgentRankByTotalPayments(String airlineName, Date dateFrom, Date dateTo);

    @Select("SELECT p.customer_email, COUNT(*) FROM purchases p " +
            "JOIN ticket t ON p.ticket_id = t.ticket_id " +
            "WHERE t.airline_name = #{airlineName} AND p.purchase_date > #{dateFrom} AND p.purchase_date < #{dateTo} " +
            "GROUP BY p.customer_email")
    List<Map<String, Integer>> getCustomerRankInAirline(String airlineName, Date dateFrom, Date dateTo);

    @Select("SELECT t.ticket_id, t.flight_num, f.departure_airport, f.arrival_airport, f.departure_time, f.arrival_time, f.price, f.status " +
            "FROM ticket t JOIN flight f ON t.flight_num = f.flight_num " +
            "WHERE t.customer_email = #{email} AND t.airline_name = #{airlineName}")
    @Results()
    List<Record> getTicketsByEmailInAirline(String email, String airlineName);

    @Select("SELECT COUNT(*) as total FROM ticket t " +
            "JOIN purchases p ON t.ticket_id = p.ticket_id " +
            "WHERE t.airline_name = #{airlineName} AND p.purchase_date > #{dateFrom} AND p.purchase_date < #{dateTo}")
    @ResultType(java.lang.Integer.class)
    Integer getTotalSales(String airlineName, Date dateFrom, Date dateTo);

    @Select("SELECT ap.name, ap.city, COUNT(*) as total FROM purchase p " +
            "JOIN ticket t ON p.ticket_id = t.ticket_id " +
            "JOIN flight f ON f.flight_num = t.flight_num " +
            "JOIN airport ap ON ap.name = flight.arrival_airport " +
            "WHERE p.purchase_date > #{dateFrom} AND p.purchase_date < #{dateTo} " +
            "GROUP BY ap.name " +
            "ORDER BY total DESC LIMIT #{limits}")
    List<Map<String, Integer>> getDestinations(Date dateFrom, Date dateTo, int limits);
}

