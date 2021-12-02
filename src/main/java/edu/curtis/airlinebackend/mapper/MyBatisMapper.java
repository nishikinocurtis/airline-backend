package edu.curtis.airlinebackend.mapper;

import edu.curtis.airlinebackend.entity.*;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MyBatisMapper {
    @Select("SELECT * FROM purchases WHERE purchases.customer_email = #{email}" +
            "JOIN customer ON purchase.customer_email = customer.email" +
            "JOIN ticket ON ticket.ticket_id = purchase.ticket_id" +
            "JOIN flight ON ticket.flight_num = flight.flight_num")
    List<Record> getTicketsByEmail(String email);

    @Select("SELECT SUM(t.price) FROM purchase p" +
            "WHERE p.customer_email = #{email} AND p.purchase_date >= #{dateFrom} AND p.purchase_date <= #{dateTo}" +
            "JOIN ticket t ON t.ticket_id = p.ticket_id")
    int getTotalPaymentsByEmailAndDate(String email, Date dateFrom, Date dateTo);

}
