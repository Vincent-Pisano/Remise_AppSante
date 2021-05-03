package ministry.ramq.repository;

import ministry.ramq.model.Citizen;
import ministry.ramq.model.CitizenInfosCovid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CitizenInfosCovidRepository extends JpaRepository<CitizenInfosCovid, Integer> {

    CitizenInfosCovid findByCitizenAndIsCurrentIsTrue(Citizen citizen);
}
