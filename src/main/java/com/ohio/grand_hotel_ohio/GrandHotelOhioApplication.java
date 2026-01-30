package com.ohio.grand_hotel_ohio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;

@SpringBootApplication
@EntityScan
public class GrandHotelOhioApplication {

	public static void main(String[] args) {
		SpringApplication.run(GrandHotelOhioApplication.class, args);

	}

}
