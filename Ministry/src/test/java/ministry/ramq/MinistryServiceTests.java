package ministry.ramq;

import ministry.ramq.model.Citizen;
import ministry.ramq.model.CitizenInfosCovid;
import ministry.ramq.repository.CitizenInfosCovidRepository;
import ministry.ramq.repository.CitizenRepository;
import ministry.ramq.service.MinistryService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ComponentScan(basePackages = {"ministry.ramq.service"})
@TestPropertySource(locations = "classpath:application-test.properties")
public class MinistryServiceTests {

    @Autowired
    private MinistryService ministryService;

    @Autowired
    private CitizenRepository citizenRepository;

    @Autowired
    private CitizenInfosCovidRepository citizenInfosCovidRepository;

    private Citizen citizen;
    private CitizenInfosCovid citizenInfosCovid;

    @BeforeAll
    public void insertData() {
        Citizen c1 = new Citizen();
        c1.setNAS("H6967");
        c1.setLastName("Lorgan");
        c1.setFirstName("Kévin");
        c1.setEmail("keke.lorg@gmail.com");
        c1.setPassword("Keke5427$");
        c1.setSex("Homme");
        c1.setBirthDate(LocalDate.of(1966, 2, 25));
        c1.setCity("Quebec");
        c1.setAddress("106 rue des Moineaux");
        c1.setPostalCode("H6D 5R5");
        c1.setPhoneNbr("5142589864");

        Citizen c2 = new Citizen();
        c2.setActive(false);
        c2.setPassword("BaJukL65");
        c2.setNAS("T4321");
        c2.setAddress("601 rue du Bonheur");
        c2.setBirthDate(LocalDate.of(1996, 7, 30));
        c2.setCity("Montréal");
        c2.setEmail("babouchka@gmail.com");
        c2.setFirstName("Baboucha");
        c2.setLastName("Tatuwu");
        c2.setPhoneNbr("5144568524");
        c2.setPostalCode("J5Y 8K8");
        c2.setSex("Autre");

        List<Citizen> citizenData = Arrays.asList(c1, c2);
        this.citizenRepository.saveAll(citizenData);

        CitizenInfosCovid cInfos1 = new CitizenInfosCovid();
        cInfos1.setCitizen(c1);
        cInfos1.setDateTest(LocalDate.now().minusDays(2));
        cInfos1.setType("TEST");
        cInfos1.setResults("POSITIVE");

        CitizenInfosCovid cInfos2 = new CitizenInfosCovid();
        cInfos2.setCitizen(c1);
        cInfos2.setDateTest(LocalDate.now().minusDays(20));
        cInfos2.setType("TEST");
        cInfos2.setResults("NEGATIVE");

        CitizenInfosCovid cInfos3 = new CitizenInfosCovid();
        cInfos3.setCitizen(c2);
        cInfos3.setDateTest(LocalDate.now().minusDays(2));
        cInfos3.setType("VACCIN");
        cInfos3.setNbrDose("1");

        List<CitizenInfosCovid> citizenInfosData = Arrays.asList(cInfos1, cInfos2, cInfos3);
        this.citizenInfosCovidRepository.saveAll(citizenInfosData);
    }

    @AfterEach
    private void clean() {
        citizenRepository.delete(citizen);
        citizenInfosCovidRepository.delete(citizenInfosCovid);
    }

    @BeforeEach
    private void generateDataCitizen() {
        citizen = new Citizen();
        citizen.setPassword("PassRA90");
        citizen.setNAS("HI87k");
        citizen.setAddress("303 boulevard DesChamps");
        citizen.setBirthDate(LocalDate.of(1998, 12, 4));
        citizen.setCity("Québec");
        citizen.setEmail("jano@gmail.com");
        citizen.setFirstName("Jean");
        citizen.setLastName("DuJardin");
        citizen.setPhoneNbr("5141573625");
        citizen.setPostalCode("J4H 7I7");
        citizen.setSex("Homme");

        citizenInfosCovid = new CitizenInfosCovid();
        citizenInfosCovid.setCitizen(citizen);
        citizenInfosCovid.setDateTest(LocalDate.now().minusDays(1));
        citizenInfosCovid.setType("TEST");
        citizenInfosCovid.setResults("NEGATIVE");

        this.citizenRepository.save(citizen);
        citizenInfosCovidRepository.save(citizenInfosCovid);
    }

    @Test
    //@Disabled
    public void testFindAllCitizen() {
        assertEquals(3, citizenRepository.findAll().size());
    }

    @Test
    //@Disabled
    public void testFindAllCitizenInfosCOVID() {
        assertEquals(4, citizenInfosCovidRepository.findAll().size());
    }


    @Test
    //@Disabled
    public void getCitizenTest() {

        assertNotNull(ministryService.getCitizen(citizen.getNAS()));
    }

    @Test
    //@Disabled
    public void isCitizenValidTest() {

        assertNotNull(ministryService.isCitizenValid(citizen));
    }

    @Test
    //@Disabled
    public void getInfosPermitCovidTest() {

        assertNotNull(ministryService.getInfosPermitCovid(citizen.getNAS()));
    }

}
