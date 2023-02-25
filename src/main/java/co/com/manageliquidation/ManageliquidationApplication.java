package co.com.manageliquidation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableFeignClients
public class ManageliquidationApplication {

	public static void main(String[] args) {
		SpringApplication.run(ManageliquidationApplication.class, args);
	}
	
}
