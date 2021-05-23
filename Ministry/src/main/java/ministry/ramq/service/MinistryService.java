package ministry.ramq.service;

import ministry.ramq.model.Citizen;
import ministry.ramq.model.CitizenInfosCovid;
import ministry.ramq.model.CredentialsPermit;
import ministry.ramq.repository.CitizenInfosCovidRepository;
import ministry.ramq.repository.CitizenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MinistryService {

    @Autowired
    private CitizenRepository citizenRepository;

    @Autowired
    private CitizenInfosCovidRepository citizenInfosCovidRepository;

    public Citizen isCitizenValid(Citizen citizen) {
        try {
            if (citizen != null) {
                System.out.println(citizen.toString());
                Citizen citizenRAMQ = citizenRepository.findByNASAndPasswordAndLastNameAndFirstNameAndBirthDate(
                        citizen.getNAS(), citizen.getPassword(), citizen.getLastName(), citizen.getFirstName(), citizen.getBirthDate());
                if (citizenRAMQ != null && citizenRAMQ.isActive()) {
                    return citizenRAMQ;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Citizen getCitizen(String NAS) {
        try {
            Citizen citizen = citizenRepository.findByNAS(NAS);
            if (citizen != null) {
                return citizen;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public CredentialsPermit getInfosPermitCovid(String NAS) {
        Citizen citizen = getCitizen(NAS);
        if (citizen != null) {
            try {
                CitizenInfosCovid citizenInfosCovid = citizenInfosCovidRepository.findByCitizenAndIsCurrentIsTrue(citizen);
                if (citizenInfosCovid != null) {
                    CredentialsPermit credentialsPermit = new CredentialsPermit();
                    credentialsPermit.setType(citizenInfosCovid.getType());
                    credentialsPermit.setDateTest(citizenInfosCovid.getDateTest());
                    if (credentialsPermit.getType().equals("VACCIN")) {
                        credentialsPermit.setNbrDose(Integer.parseInt(citizenInfosCovid.getNbrDose()));
                    } else {
                        credentialsPermit.setResults(citizenInfosCovid.getResults());
                    }
                    return credentialsPermit;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        return null;
    }
}
