package ministry.ramq.repository;

import ministry.ramq.model.Citizen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface CitizenRepository extends JpaRepository<Citizen, Integer> {

    Citizen findByNAS(String NAS);

    Citizen findByNASAndPasswordAndLastNameAndFirstNameAndBirthDate(String nas, String password, String lastName, String firstName, LocalDate birthDate);
}
