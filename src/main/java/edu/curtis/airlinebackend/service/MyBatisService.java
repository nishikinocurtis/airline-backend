package edu.curtis.airlinebackend.service;

import edu.curtis.airlinebackend.entity.*;
import edu.curtis.airlinebackend.mapper.MyBatisMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

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

}
