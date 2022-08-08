package io.github.beinlichsimone.vacationtrip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableCaching
@EnableSpringDataWebSupport //paginação e ordenação
@SpringBootApplication
@EnableSwagger2
public class VacationTripApplication {

	public static void main(String[] args) {
		SpringApplication.run(VacationTripApplication.class, args);
	}

}
