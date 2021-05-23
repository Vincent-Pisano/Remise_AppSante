package com.permissante.admin.repositories;

import com.permissante.admin.models.Citizen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CitizenRepository extends JpaRepository<Citizen, Integer> {

    Citizen findByEmailAndPassword(String email, String password);

    Citizen findCitizenByEmail(String email);

    Citizen findCitizenByNAS(String nas);

    Citizen findByChildListContains(Citizen citizen);
}
