package ministry.ramq.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ministry.ramq.model.Citizen;
import ministry.ramq.model.CredentialsPermit;
import ministry.ramq.service.MinistryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;

@RestController
@CrossOrigin(origins = "http://localhost:9090")
public class MinistryController {

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

    @Autowired
    private MinistryService service;

    @GetMapping("/ministry")
    public Citizen isCitizenValid(@RequestParam String jsonCitizen) {

        Citizen citizen = null;
        try {
            citizen = new ObjectMapper().setDateFormat(simpleDateFormat).readValue(jsonCitizen, Citizen.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return service.isCitizenValid(citizen);
    }

    @GetMapping("/ministry/getCitizen/{NAS}")
    public Citizen getCitizen(@PathVariable String NAS) {
        return service.getCitizen(NAS);
    }

    @GetMapping("/ministry/getInfosPermit/{NAS}")
    public CredentialsPermit getInfosPermit(@PathVariable String NAS) {
        return service.getInfosPermitCovid(NAS);
    }
}
