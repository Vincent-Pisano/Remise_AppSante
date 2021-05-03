package com.permissante.appsante.repositories;

import com.permissante.appsante.model.Citizen;
import com.permissante.appsante.model.PermitTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;


public interface PermitRepository extends JpaRepository<PermitTest, Integer> {

    PermitTest findByCitizenAndIsActiveIsTrue(Citizen citizen);

    List<PermitTest> findAllByCitizen(Citizen citizen);

    @Transactional
    @Modifying
    @Query("UPDATE PermitTest p set p.isActive = false where p.dateExpiration < :date")
    int desactivePermis(@Param("date") LocalDate date);

}
