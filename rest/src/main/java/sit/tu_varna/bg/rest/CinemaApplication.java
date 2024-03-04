package sit.tu_varna.bg.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "sit.tu_varna.bg")
@EntityScan(basePackages = "sit.tu_varna.bg.data.entity")
@EnableJpaRepositories(basePackages = "sit.tu_varna.bg.data.repository")
public class CinemaApplication {

    public static void main(String[] args) {
        SpringApplication.run(CinemaApplication.class, args);
    }

}
