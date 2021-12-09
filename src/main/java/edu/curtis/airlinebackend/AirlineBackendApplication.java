package edu.curtis.airlinebackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("edu.curtis.airlinebackend.mapper")
public class AirlineBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(AirlineBackendApplication.class, args);
    }

}
