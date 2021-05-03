package com.permissante.appsante;

import com.permissante.appsante.repositories.PermitRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;

@SpringBootApplication
@EnableScheduling
@Log
public class AppsanteApplication {

    @Autowired
    private PermitRepository permitRepository;

    public static void main(String[] args) {
        SpringApplication.run(AppsanteApplication.class, args);
    }

    @Scheduled(cron = "0 00 06 * * ?") // seconds minutes hours day month year
    public void deactivatePermit() {
        System.out.println("Number of updated Record --> " + permitRepository.desactivePermis(LocalDate.of(2021, 4, 25)));

    }
}
