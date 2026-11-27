package tn.esprit.ms4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * MS4 - Sleep & Mental Wellness.
 * Client Eureka : s'enregistre sous le nom MS4 aupres du serveur http://localhost:8761.
 */
@SpringBootApplication
@EnableDiscoveryClient // facultative avec Spring Cloud recent : rend le role de client explicite
public class Ms4Application {

    public static void main(String[] args) {
        SpringApplication.run(Ms4Application.class, args);
    }

}
