package com.permissante.admin;

import com.permissante.admin.models.Citizen;
import com.permissante.admin.models.PermitTest;
import com.permissante.admin.models.PermitVaccin;
import com.permissante.admin.repositories.CitizenRepository;
import com.permissante.admin.repositories.PermitRepository;
import com.permissante.admin.service.AdminService;
import javassist.NotFoundException;
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
@ComponentScan(basePackages = {"com.permissante.admin.service"})
@TestPropertySource(locations = "classpath:application-test.properties")
public class AdminServiceTests {

    @Autowired
    private CitizenRepository citizenRepository;

    @Autowired
    private PermitRepository permitRepository;

    @Autowired
    private AdminService service;

    private Citizen citizen;
    private PermitTest permit;

    @BeforeAll
    public void InsertData() {

        Citizen c1 = new Citizen();
        c1.setNAS("H6967");
        c1.setLastName("Lorgan");
        c1.setFirstName("Kévin");
        c1.setEmail("keke.lorg@gmail.com");
        c1.setPassword("Keke5427$");
        c1.setSex("Homme");
        c1.setBirthDate(LocalDate.of(1967, 2, 25));
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

        PermitVaccin p1 = new PermitVaccin();
        p1.setDateTest(LocalDate.now());
        p1.setDateExpiration(LocalDate.now().plusDays(60));
        p1.setNbrDose(1);
        p1.setCitizen(c1);
        PermitTest p2 = new PermitTest();
        p2.setDateTest(LocalDate.now());
        p1.setDateExpiration(LocalDate.now().plusDays(15));
        p2.setActive(false);
        p2.setCitizen(c2);
        PermitVaccin p3 = new PermitVaccin();
        p3.setDateTest(LocalDate.now());
        p1.setDateExpiration(LocalDate.now().plusDays(365));
        p3.setNbrDose(2);
        PermitTest p4 = new PermitTest();
        p4.setDateTest(LocalDate.now());
        p1.setDateExpiration(LocalDate.now().plusDays(15));

        List<PermitTest> permisData = Arrays.asList(p1, p2, p3, p4);
        this.permitRepository.saveAll(permisData);
    }

    @AfterEach
    private void clean() {
        permitRepository.delete(permit);
        citizenRepository.delete(citizen);
    }

    @BeforeEach
    private void generateDataCitizen() {
        citizen = new Citizen();
        citizen.setPassword("$hfg$254");
        citizen.setNAS("JODNZ52");
        citizen.setAddress("114 rue Joviale");
        citizen.setBirthDate(LocalDate.of(1982, 4, 15));
        citizen.setCity("Saint-Jean");
        citizen.setEmail("cleinden@gmail.com");
        citizen.setFirstName("Christophe");
        citizen.setLastName("Leindecker");
        citizen.setPhoneNbr("5143587539");
        citizen.setPostalCode("J67 6U5");
        citizen.setSex("Homme");

        permit = new PermitTest();
        permit.setDateTest(LocalDate.now());
        permit.setDateExpiration(LocalDate.now().plusDays(60));
        permit.setCitizen(citizen);
    }

    @Test
    @Disabled
    public void getAllCitizenTest() {
        assertEquals(2, service.getAllCitizen().size());
    }

    @Test
    @Disabled
    public void getCitizenTest() {
        Citizen citizen = citizenRepository.save(this.citizen);
        assertNotNull(service.getCitizen(citizen.getIdCitizen()));
    }

    @Test
    @Disabled
    public void deactivateCitizenTest() {
        service.deactivateCitizen(citizenRepository.save(citizen).getIdCitizen());
        assertFalse(citizenRepository.findCitizenByNAS(citizen.getNAS()).isActive());
    }

    @Test
    @Disabled
    public void getAllPermitTest() {
        assertEquals(4, service.getAllPermit().size());
    }

    @Test
    @Disabled
    public void getPermitTest() {
        PermitTest permit = permitRepository.save(this.permit);
        assertNotNull(service.getPermit(permit.getIdPermit()));
    }

    @Test
    @Disabled
    public void deactivatePermitTest() {
        PermitTest permit = permitRepository.save(this.permit);
        service.deactivatePermit(permit.getIdPermit());
        assertFalse(permitRepository.getOne(permit.getIdPermit()).isActive());
    }

    @Test
    @Disabled
    public void updateChildListTest() {
        citizenRepository.save(citizen);
        try {
            Citizen parent = service.updateChildList(citizen.getNAS(), "T4321", null);
            assertEquals(1, parent.getChildList().size());
        } catch (NotFoundException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @Disabled
    public void updatePermitTest() {
        Citizen citizen = citizenRepository.save(this.citizen);
        try {
            PermitTest permit = service.updatePermit(null, LocalDate.now(), LocalDate.now().plusDays(15),
                    null, citizen.getIdCitizen(), null);
            assertNotNull(permit);
        } catch (NotFoundException e) {
            e.printStackTrace();
            fail();
        }
    }
}
