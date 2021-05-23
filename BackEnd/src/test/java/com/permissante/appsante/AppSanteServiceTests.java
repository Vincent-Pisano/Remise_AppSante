package com.permissante.appsante;

import com.permissante.appsante.model.*;
import com.permissante.appsante.repositories.CitizenRepository;
import com.permissante.appsante.repositories.PermitRepository;
import com.permissante.appsante.services.AppSanteService;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.jupiter.api.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@DataJpaTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ComponentScan(basePackages = {"com.permissante.appsante.services"})
public class AppSanteServiceTests {

    @Autowired
    private AppSanteService appSanteService;

    @Autowired
    private CitizenRepository citizenRepository;

    @Autowired
    private PermitRepository permitRepository;

    @Autowired
    private Environment environment;

    private PermitTest permit;
    private Citizen citizen;

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

        PermitVaccin p1 = new PermitVaccin();
        p1.setDateTest(LocalDate.now());
        p1.setDateExpiration(LocalDate.now().plusDays(Integer.parseInt(Objects.requireNonNull(environment.getProperty("permit.dateExpiration.vaccine.dose1")))));
        p1.setNbrDose(1);
        p1.setCitizen(c1);
        PermitTest p2 = new PermitTest();
        p2.setDateTest(LocalDate.now());
        p1.setDateExpiration(LocalDate.now().plusDays(Integer.parseInt(Objects.requireNonNull(environment.getProperty("permit.dateExpiration.test")))));
        p2.setActive(false);
        p2.setCitizen(c2);
        PermitVaccin p3 = new PermitVaccin();
        p3.setDateTest(LocalDate.now());
        p1.setDateExpiration(LocalDate.now().plusDays(Integer.parseInt(Objects.requireNonNull(environment.getProperty("permit.dateExpiration.vaccine.dose2")))));
        p3.setNbrDose(2);
        PermitTest p4 = new PermitTest();
        p4.setDateTest(LocalDate.now());
        p1.setDateExpiration(LocalDate.now().plusDays(Integer.parseInt(Objects.requireNonNull(environment.getProperty("permit.dateExpiration.test")))));

        List<PermitTest> permisData = Arrays.asList(p1, p2, p3, p4);
        this.permitRepository.saveAll(permisData);
    }

    @AfterEach
    private void clean() {
        permitRepository.delete(permit);
        cleanFilesOfCitizen();
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
    }

    /*
        Début des tests
    */

    @Test
    @Disabled
    public void testFindAllPermis() {
        assertEquals(4, permitRepository.findAll().size());
    }

    @Test
    @Disabled
    public void testFindAllCitizen() {
        assertEquals(2, citizenRepository.findAll().size());
    }

    @Test
    @Disabled
    public void testLogin() {
        assertNotNull(appSanteService.login("keke.lorg@gmail.com", "Keke5427$"));
    }

    @Test
    @Disabled
    public void testIsLoginExist() {
        assertTrue(appSanteService.isLoginExist("babouchka@gmail.com"));
    }

    @Test
    @Disabled
    public void testIsAccountExist() {
        assertTrue(appSanteService.isAccountExist("H6967"));
    }

    @Test
    @Disabled
    public void testRefusal() {
        assertEquals(appSanteService.refusal("Refus"),
                Objects.requireNonNull(environment.getProperty("refusal.text")) + " Refus");
    }

    @Test
    @Disabled
    public void testGenerateQR() {
        String filepath = getFilePathQR(citizen);
        permit.setCitizen(citizen);
        try {
            appSanteService.generateQR(permit, filepath);
            File qrCode = new File(filepath);

            assertTrue(qrCode.exists());
            assertNotNull(permit.getQrcode());
        } catch (Exception ex) {
            ex.printStackTrace();
            fail();
        }
    }

    @Test
    @Disabled
    public void testGeneratePDF() {
        //on préfererait avoir un QRCode existant dans un fichier pour ne pas dépendre de
        //appSanteService.generateQR(), mais la méthode de remise du projet ne permet pas cela.
        String filepathQR = getFilePathQR(citizen);
        String filepathPDF = getFilePathPDF(citizen);
        permit.setCitizen(citizen);
        try {
            appSanteService.generateQR(permit, filepathQR);
            appSanteService.generatePDF(filepathPDF, filepathQR);
            File pdf = new File(filepathPDF);

            assertTrue(pdf.exists());
        } catch (Exception ex) {
            ex.printStackTrace();
            fail();
        }
    }

    @Test
    @Disabled
    public void testGenerateData() {
        permit.setCitizen(citizen);
        try {
            appSanteService.generateData(permit);

            String filepathQR = getFilePathQR(citizen);
            String filepathPDF = getFilePathPDF(citizen);
            File pdf = new File(filepathPDF);
            File qr = new File(filepathQR);

            assertTrue(pdf.exists());
            assertTrue(qr.exists());
        } catch (Exception ex) {
            ex.printStackTrace();
            fail();
        }
    }

    @Test
    @Disabled
    public void testCreatePermitVaccin() {
        permit.setCitizen(citizen);
        String filepathQR = getFilePathQR(citizen);
        String filepathPDF = getFilePathPDF(citizen);
        File pdf = new File(filepathPDF);
        File qr = new File(filepathQR);

        assertTrue(appSanteService.createPermitVaccin(citizen, LocalDate.now(), 2));

        assertNotNull(citizenRepository.findCitizenByNAS(permit.getCitizen().getNAS()));
        assertNotNull(permitRepository.findById(permit.getIdPermit()));

        assertTrue(pdf.exists());
        assertTrue(qr.exists());
    }

    @Test
    @Disabled
    public void testCreatePermitTest() {
        permit.setCitizen(citizen);
        String filepathQR = getFilePathQR(citizen);
        String filepathPDF = getFilePathPDF(citizen);
        File pdf = new File(filepathPDF);
        File qr = new File(filepathQR);

        assertTrue(appSanteService.createPermitTest(citizen, LocalDate.now()));

        assertNotNull(citizenRepository.findCitizenByNAS(permit.getCitizen().getNAS()));
        assertNotNull(permitRepository.findById(permit.getIdPermit()));

        assertTrue(pdf.exists());
        assertTrue(qr.exists());
    }

    @Test
    @Disabled
    public void testGetPermit() {
        permit.setCitizen(citizen);
        citizenRepository.save(citizen);
        permitRepository.save(permit);

        assertEquals(appSanteService.getPermit(citizen.getNAS()), permit);
    }

    @Test
    @Disabled
    public void testIsCitizenValid() {
        citizen = new Citizen();
        citizen.setNAS("JODNZ52");
        citizen.setPassword("$hfg$254");
        citizen.setBirthDate(LocalDate.of(1982, 4,15));
        citizen.setLastName("Leindecker");
        citizen.setFirstName("Christophe");

        assertNotNull(appSanteService.isCitizenValid(citizen));
    }

    @Test
    @Disabled
    public void testGetCitizenFromRAMQ() {
        assertNotNull(appSanteService.getCitizenFromRAMQ("JODNZ52"));
    }

    @Test
    @Disabled
    public void testGetCredentialsFromRAMQ() {
        assertNotNull(appSanteService.getCredentialsFromRAMQ("JODNZ52"));
    }

    @Test
    @Disabled
    public void testCreateAccount() {
        assertEquals(Objects.requireNonNull(environment.getProperty("success.message.create.account")),
                appSanteService.createAccount(citizen));
    }

    @Test
    @Disabled
    public void testCreateAccountMinor() {
        Citizen citizenMinor = new Citizen();
        citizenMinor.setPassword("Eli258");
        citizenMinor.setNAS("AZUK5QE");
        citizenMinor.setAddress("114 rue Joviale");
        citizenMinor.setBirthDate(LocalDate.of(2012, 11, 9));
        citizenMinor.setCity("Saint-Jean");
        citizenMinor.setEmail("Elies2598@gmail.com");
        citizenMinor.setFirstName("Eliès");
        citizenMinor.setLastName("Leindecker");
        citizenMinor.setPhoneNbr("5148963575");
        citizenMinor.setPostalCode("J67 6U5");
        citizenMinor.setSex("Homme");
        citizen.setChildList(new ArrayList<>());
        citizen.getChildList().add(citizenMinor);

        assertEquals(Objects.requireNonNull(environment.getProperty("success.message.create.account")),
                appSanteService.createAccount(citizen, citizenMinor));
    }

    /*
        Fin des tests
    */

      /*
        Data management methods post-refactor
     */

    private boolean cleanFilesOfCitizen() {
        String filepath = Objects.requireNonNull(environment.getProperty("file.path"))
                + "/" + citizen.getNAS();
        File directory = new File(filepath);
        if (directory.exists()) {
            try {
                FileUtils.deleteDirectory(directory);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }


    private String getFilePathQR(Citizen citizen) {
        return Objects.requireNonNull(environment.getProperty("file.path"))
                + "/" + citizen.getNAS() + "/" + LocalDate.now().toString() + "/" + citizen.getNAS() +
                Objects.requireNonNull(environment.getProperty("file.extension.png"));
    }

    private String getFilePathPDF(Citizen citizen) {
        return Objects.requireNonNull(environment.getProperty("file.path"))
                + "/" + citizen.getNAS() + "/" + LocalDate.now().toString() + "/" + citizen.getNAS() +
                Objects.requireNonNull(environment.getProperty("file.extension.pdf"));
    }
}
