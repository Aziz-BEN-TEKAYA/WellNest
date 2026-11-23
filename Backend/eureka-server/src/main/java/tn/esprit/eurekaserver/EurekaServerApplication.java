package tn.esprit.eurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Annuaire des microservices WellNest : chaque MS s'y enregistre sous son
 * spring.application.name, et la Gateway le consulte pour router (lb://MS2...).
 * Tableau de bord : http://localhost:8761
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
    }
}
