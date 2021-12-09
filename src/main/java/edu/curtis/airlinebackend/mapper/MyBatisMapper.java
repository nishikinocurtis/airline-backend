package edu.curtis.airlinebackend.mapper;

import edu.curtis.airlinebackend.entity.*;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.javassist.compiler.ast.Pair;
import org.springframework.stereotype.Repository;
import java.lang.Exception;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface MyBatisMapper {
    // Public Part
    @Insert("INSERT INTO ticket VALUES (#{ticketId}, #{airlineName}, #{flightNum})")
    void insertTickets(Ticket t) throws Exception;

    @Insert("INSERT INTO purchases VALUES (#{ticketId}, #{customerEmail}, #{bookingAgentId}, #{purchaseDate})")
    void insertPurchase(Purchase p) throws Exception;

    @Select("SELECT * FROM flight f " +
            "WHERE departure_airport = #{deptPort} AND arrival_airport = #{arriPort} AND deptDate = #{date}")
    List<Flight> getFlightByDeptArriDate(String deptPort, String arriPort, Date date);

    @Select("SELECT email FROM customer WHERE email = #{Id} AND password = #{pwd}")
    List<String> loginVerification(@Param("Id")String Id, @Param("pwd")String pwd, @Param("role")String role);

    @Select("SELECT booking_agent_id FROM booking_agent WHERE booking_agent_id = #{Id} AND password = #{pwd}")
    List<String> agentLoginVerification(@Param("Id")String Id, @Param("pwd")String pwd);

    //TODO: Deal with pattern %% in Service.
    @Select("SELECT * FROM airport ap " +
            "WHERE airport_name LIKE #{search} OR airport_city LIKE #{search}")
    @Results(id="airport",
             value = {
            @Result(property = "name", column = "airport_name", id = true),
            @Result(property = "city", column = "airport_city")
    }
    )
    List<Airport> getAirport(String search);

    @Select("SELECT * FROM flight f " +
            "WHERE f.departure_airport = #{deptPort} AND f.arrival_airport = #{arriPort} AND f.departure_time > #{dateFrom} AND f.departure_time < #{dateTo}")
    @Results(id="flight",
            value = {
                    @Result(property = "flightNum", column = "flight_num", id = true),
                    @Result(property = "airlineName", column = "airline_name"),
                    @Result(property = "departurePort", column = "departure_airport"),
                    @Result(property = "departureTime", column = "departure_time"),
                    @Result(property = "arrivalPort", column = "arrival_airport"),
                    @Result(property = "arrivalTime", column = "arrival_time"),
                    @Result(property = "status", column = "status"),
                    @Result(property = "airplaneId", column = "airplane_id"),
                    @Result(property = "price", column = "price")
            }
    )
    List<Flight> getFlight(String deptPort, String arriPort, Date dateFrom, Date dateTo) throws Exception;

    @Insert("INSERT INTO customer VALUES (#{email}, #{name}, #{password}, #{building_number}, #{street}, #{city}, #{state}, " +
            "#{phone_number}, #{passport_number}, #{passport_exp}, #{passport_country}, #{DOB})")
    void customerRegistration(String email, String name, String password, String building_number, String street, String city,
                              String state, String phone_number, String passport_number, Date passport_exp, String passport_country,
                              Date DOB) throws Exception;

    // Customer Part
    @Select("SELECT t.ticket_id, p.customer_email, t.airline_name, t.flight_num, f.departure_airport, f.arrival_airport, f.status, f.departure_time, f.arrival_time, f.price " +
            "FROM purchases p " +
            "JOIN ticket t ON t.ticket_id = p.ticket_id " +
            "JOIN flight f ON t.flight_num = f.flight_num " +
            "WHERE (p.customer_email = #{email})")
    @Results(id = "record",
            value = {
                    @Result(property = "ticketId", column = "ticket_id", id = true),
                    @Result(property = "email", column = "customer_email"),
                    @Result(property = "deptPort", column = "departure_airport"),
                    @Result(property = "deptTime", column = "departure_time"),
                    @Result(property = "arriPort", column = "arrival_airport"),
                    @Result(property = "arriTime", column = "arrival_time"),
                    @Result(property = "status", column = "status"),
                    @Result(property = "flightNum", column = "flight_num"),
                    @Result(property = "airlineName", column = "airline_name"),
                    @Result(property = "price", column = "price")
            }
    )
    List<Record> getTicketsByEmail(String email) throws Exception;
    //TODO: Modify

    @Select("SELECT SUM(f.price) FROM purchases p " +
            "JOIN ticket t ON t.ticket_id = p.ticket_id " +
            "JOIN flight f ON f.flight_num = t.flight_num " +
            "WHERE p.customer_email = #{email} AND p.purchase_date >= #{dateFrom} AND p.purchase_date <= #{dateTo}")
    Double getTotalPaymentsByEmailAndDate(String email, Date dateFrom, Date dateTo);

    // Agent Part
    @Select("SELECT t.ticket_id, p.customer_email, t.airline_name, t.flight_num, f.departure_airport, f.arrival_airport, f.status, f.departure_time, f.arrival_time, f.price  " +
            "FROM purchases p " +
            "JOIN customer c ON p.customer_email = c.email " +
            "JOIN ticket t ON t.ticket_id = p.ticket_id " +
            "JOIN flight f ON t.flight_num = f.flight_num " +
            "WHERE p.booking_agent_id = #{agentId}")
    @ResultMap("record")
    List<Record> getTicketsById(String agentId);
    //TODO: modify

    @Select("SELECT SUM(f.price) FROM purchases p " +
            "JOIN ticket t ON t.ticket_id = p.ticket_id " +
            "JOIN flight f ON t.flight_num = f.flight_num " +
            "WHERE p.booking_agent_id = #{agentId} AND p.purchase_date >= #{dateFrom} AND p.purchase_date <= #{dateTo} ")
    Double getTotalSellsByIdAndDate(String agentId, Date dateFrom, Date dateTo);

    @Select("SELECT c2.email, COUNT(*) num FROM purchases p " +
            "JOIN ticket t ON t.ticket_id = p.ticket_id " +
            "JOIN flight f ON t.flight_num = f.flight_num " +
            "JOIN customer c2 on p.customer_email = c2.email " +
            "WHERE p.booking_agent_id = #{agentId} AND p.purchase_date >= #{dateFrom} AND p.purchase_date <= #{dateTo} " +
            "GROUP BY c2.email")
    @Results(id = "statistic",
            value = {
            @Result(property = "key", column = "email", id = true),
            @Result(property = "value", column = "num")
    })
    List<KVData> getCustomerRankByTicketNumber(String agentId, Date dateFrom, Date dateTo);

    @Select("SELECT c.email, SUM(f.price) num FROM purchases p " +
            "JOIN ticket t ON t.ticket_id = p.ticket_id " +
            "JOIN flight f ON t.flight_num = f.flight_num " +
            "JOIN customer c on p.customer_email = c.email " +
            "WHERE p.booking_agent_id = #{agentId} AND p.purchase_date >= #{dateFrom} AND p.purchase_date <= #{dateTo} " +
            "GROUP BY c.email")
    @ResultMap("statistic")
    List<KVData> getCustomerRankByTicketPrice(String agentId, Date dateFrom, Date dateTo);

    @Select("SELECT * FROM booking_agent_work_for WHERE booking_agent_id = #{email} AND airline_name = #{airlineName}")
    List<StringPair> affiliationValidation(String email, String airlineName);

    // Staff Part
    @Select("SELECT airline_name FROM airline_staff WHERE username = #{username}")
    List<String> getAirline(String username);

    @Select("SELECT * FROM flight WHERE airline_name = #{airlineName}")
    List<Flight> viewFlights(String airlineName);

    @Select("SELECT * FROM flight WHERE airline_name = #{airlineName} AND departure_time >= #{dateFrom} AND departure_time <= #{dateTo}")
    @ResultMap("flight")
    List<Flight> viewFlightsByDate(String airlineName, Date dateFrom, Date dateTo);

    @Select("SELECT p.customer_email FROM ticket t JOIN purchases p on t.ticket_id = p.ticket_id WHERE t.flight_num = #{flight}")
    List<String> viewCustomersByFlight(String flight);

    @Insert("INSERT INTO flight VALUES (#{flightNum}, #{airlineName}, #{departurePort}, " +
            "#{departureTime}, #{arrivalPort}, #{arrivalTime}, #{price}, #{status}, #{airplaneId})")
    void createFlight(Flight f) throws Exception;

    @Update("UPDATE flight SET status = #{newStatus} WHERE airline_name = #{airlineName} AND " +
            "flight_num = #{flightNum}")
    void flipFlightStatus(String airlineName, String flightNum, Date deptTime, String newStatus) throws Exception;

    @Insert("INSERT INTO airplane VALUES (#{airlineName}, #{airplaneId}, #{seats})")
    void createAirplane(Airplane a) throws Exception;

    @Insert("INSERT INTO airport VALUES (#{name}, #{city})")
    void createAirport(Airport a);

    @Insert("INSERT INTO booking_agent VALUES (#{bookingAgentId}, #{email}, #{password})")
    void insertBookingAgent(BookingAgent b);

    @Insert("INSERT INTO booking_agent_work_for VALUES (#{email}, #{airlineName})")
    void addAgentToAirline(BookingAgentAffiliation af);

    @Insert("INSERT INTO permission VALUES (#{username}, #{priv})")
    void grantPermission(String username, String priv); // Staff username and privilege.

    @Select("SELECT permission_type pt FROM permission WHERE username = #{username}")
    List<String> getPermissionList(String username);

    // Statistics Part
    @Select("SELECT p.booking_agent_id email, COUNT(*) num FROM purchases p " +
            "JOIN ticket t ON p.ticket_id = t.ticket_id " +
            "WHERE t.airline_name = #{airlineName} AND p.purchase_date > #{dateFrom} AND p.purchase_date < #{dateTo} " +
            "GROUP BY p.booking_agent_id " +
            "ORDER BY num DESC LIMIT #{limits}")
    @ResultMap("statistic")
    List<KVData> getAgentRankByTicketNumber(String airlineName, Date dateFrom, Date dateTo, int limits);

    @Select("SELECT p.booking_agent_id email, SUM(f.price) num FROM purchases p " +
            "JOIN ticket t ON p.ticket_id = t.ticket_id " +
            "JOIN flight f ON f.flight_num = t.flight_num " +
            "WHERE t.airline_name = #{airlineName} AND p.purchase_date >= #{dateFrom} AND p.purchase_date <= #{dateTo} " +
            "GROUP BY p.booking_agent_id " +
            "ORDER BY num DESC LIMIT #{limits}")
    @ResultMap("statistic")
    List<KVData> getAgentRankByTotalPayments(String airlineName, Date dateFrom, Date dateTo, int limits);

    @Select("SELECT p.customer_email email, COUNT(*) num FROM purchases p " +
            "JOIN ticket t ON p.ticket_id = t.ticket_id " +
            "WHERE t.airline_name = #{airlineName} AND p.purchase_date > #{dateFrom} AND p.purchase_date < #{dateTo} " +
            "GROUP BY p.customer_email " +
            "ORDER BY num DESC LIMIT #{limits}")
    @ResultMap("statistic")
    List<KVData> getCustomerRankInAirline(String airlineName, Date dateFrom, Date dateTo, int limits);

    @Select("SELECT t.ticket_id, p.customer_email, t.airline_name, t.flight_num, f.departure_airport, f.arrival_airport, f.status, f.departure_time, f.arrival_time, f.price " +
            "FROM ticket t JOIN flight f ON t.flight_num = f.flight_num " +
            "JOIN purchases p ON p.ticket_id = t.ticket_id " +
            "WHERE p.customer_email = #{email} AND t.airline_name = #{airlineName}")
    @ResultMap("record")
    List<Record> getTicketsByEmailInAirline(String email, String airlineName);

    @Select("SELECT COUNT(*) as total FROM ticket t " +
            "JOIN purchases p ON t.ticket_id = p.ticket_id " +
            "WHERE t.airline_name = #{airlineName} AND p.purchase_date > #{dateFrom} AND p.purchase_date < #{dateTo}")
    @ResultType(java.lang.Integer.class)
    Integer getTotalSales(String airlineName, Date dateFrom, Date dateTo);

    @Select("SELECT ap.airport_name email, COUNT(*) as num FROM purchases p " +
            "JOIN ticket t ON p.ticket_id = t.ticket_id " +
            "JOIN flight f ON f.flight_num = t.flight_num " +
            "JOIN airport ap ON ap.airport_name = f.arrival_airport " +
            "WHERE p.purchase_date >= #{dateFrom} AND p.purchase_date <= #{dateTo} " +
            "GROUP BY email " +
            "ORDER BY num DESC LIMIT #{limits}")
    @ResultMap("statistic")
    List<KVData> getDestinations(Date dateFrom, Date dateTo, int limits);

    @Select("SELECT airline_name FROM airline_staff WHERE username = #{Id} AND password = #{pwd}")
    List<String> staffLoginVerification(String Id, String pwd);
}

