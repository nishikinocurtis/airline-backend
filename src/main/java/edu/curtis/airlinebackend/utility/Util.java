package edu.curtis.airlinebackend.utility;

import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.Random;

@Controller
public class Util {
    public static String genTicketId() {
        Date current = new Date();
        Random rn = new Random();
        int prefix = rn.nextInt() % 10000;
        long ticketId = current.getTime();
        String result = Long.toString(ticketId) + Integer.toString(prefix);
        return result;
    }
}
