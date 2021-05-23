package com.permissante.admin.repositories;

import com.permissante.admin.models.Citizen;
import com.permissante.admin.models.PermitTest;
import com.permissante.admin.models.PermitVaccin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PermitRepository extends JpaRepository<PermitTest, Integer> {

    List<PermitTest> findAllByCitizen(Citizen citizen);

    PermitTest findByCitizenAndIsActiveIsTrue(Citizen citizen);

}
