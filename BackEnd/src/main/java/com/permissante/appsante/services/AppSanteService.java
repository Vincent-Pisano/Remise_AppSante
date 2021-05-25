package com.permissante.appsante.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.permissante.appsante.model.*;
import com.permissante.appsante.repositories.CitizenRepository;
import com.permissante.appsante.repositories.PermitRepository;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/*
 *  This class represent ALL the service of the application
 */
@Service
public class AppSanteService {

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");


    @Autowired
    CitizenRepository citizenRepository;

    @Autowired
    private PermitRepository permitRepository;

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Autowired
    private Environment environment;

    public Citizen login(String email, String password) {
        return citizenRepository.findByEmailAndPassword(email, password);
    }

    public boolean isLoginExist(String email) {
        return (citizenRepository.findCitizenByEmail(email) != null);
    }


    public boolean isAccountExist(String NAS) {
        return (citizenRepository.findCitizenByNAS(NAS) != null);
    }

    //no test
    public boolean getForgotPassword(String email) {
        try {
            Citizen citizen = citizenRepository.findCitizenByEmail(email);
            if (citizen != null) {
                return sendEmail(email, citizen.getPassword());
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    //no test
    public String renewPermit(Citizen citizen) {
        citizen = citizenRepository.findCitizenByNAS(citizen.getNAS());
        if (citizen != null) {
            try {
                CredentialsPermit credentialsPermit = getCredentialsFromRAMQ(citizen.getNAS());
                List<PermitTest> listPermit = permitRepository.findAllByCitizen(citizen);
                for (PermitTest permit : listPermit) {
                    if (permit.getDateTest().equals(credentialsPermit.getDateTest())) {
                        return refusal(Objects.requireNonNull(environment.getProperty("refusal.message.no.permit.valid")));
                    }
                }
                return completePermitInformations(citizen);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return Objects.requireNonNull(environment.getProperty("errors.fatalError"));
    }

    public String createAccount(Citizen citizen) {
        if (citizen != null) {
            if (isAccountExist(citizen.getNAS()) || isLoginExist(citizen.getEmail())) {
                return refusal(Objects.requireNonNull(environment.getProperty("refusal.message.accountAlreadyExist")));
            }
            if (citizen.getBirthDate().isAfter(LocalDate.now().minusYears(18))) {
                return refusal(Objects.requireNonNull(environment.getProperty("refusal.message.citizen.is.minor")));
            }
            citizen.setChildList(null);
            return completePermitInformations(citizen);
        }
        return Objects.requireNonNull(environment.getProperty("errors.fatalError"));
    }

    public String createAccount(Citizen citizenParent, Citizen citizenChild) {
        if (citizenParent != null && citizenChild != null) {
            //The parent is authorised to have an existing account, but not the minor
            if (isAccountExist(citizenChild.getNAS()) || isLoginExist(citizenChild.getEmail())) {
                return refusal(Objects.requireNonNull(environment.getProperty("refusal.message.accountAlreadyExist")));
            }
            for (Citizen child : citizenParent.getChildList()) {
                if (child.getNAS().equals(citizenChild.getNAS()) &&
                        child.getPassword().equals(citizenChild.getPassword()) &&
                        child.getLastName().equals(citizenChild.getLastName()) &&
                        child.getFirstName().equals(citizenChild.getFirstName()) &&
                        child.getBirthDate().equals(citizenChild.getBirthDate())) {
                    citizenChild.setChildList(null);
                    String result = completePermitInformations(citizenChild);

                    if (result.equals(Objects.requireNonNull(environment.getProperty("success.message.create.account")))) {
                        updateParent(citizenParent, citizenChild);
                    }
                    return result;
                }
            }
            return refusal(Objects.requireNonNull(environment.getProperty("refusal.message.AccountInvalid")) + citizenChild.getNAS());
        }
        return Objects.requireNonNull(environment.getProperty("errors.fatalError"));
    }

    //no test
    private void updateParent(Citizen citizenParent, Citizen citizenChild) {
        if (citizenRepository.findCitizenByNAS(citizenParent.getNAS()) == null) {
            citizenParent.setChildList(new ArrayList<>());
            citizenParent.getChildList().add(citizenRepository.findCitizenByNAS(citizenChild.getNAS()));
        }
        else {
            citizenParent = citizenRepository.findCitizenByNAS(citizenParent.getNAS());
            citizenParent.getChildList().add(citizenRepository.findCitizenByNAS(citizenChild.getNAS()));
        }
        citizenRepository.save(citizenParent);
    }

    //no test
    private String completePermitInformations(Citizen citizen) {
        boolean flag;
        CredentialsPermit credentialsPermit = getCredentialsFromRAMQ(citizen.getNAS());
        if (credentialsPermit.getDateTest().isAfter(LocalDate.now().minusDays(3))) {
            if (credentialsPermit.getType().equals("VACCIN")) {
                flag = createPermitVaccin(citizen, credentialsPermit.getDateTest(), credentialsPermit.getNbrDose());
            } else {

                if (credentialsPermit.getResults().equals("NEGATIVE")) {
                    flag = createPermitTest(citizen, credentialsPermit.getDateTest());
                } else {
                    return refusal(Objects.requireNonNull(environment.getProperty("refusal.message.testPositive")));
                }
            }
            if (!flag) {
                return refusal(Objects.requireNonNull(environment.getProperty("refusal.message.create.permit")));
            } else {
                return Objects.requireNonNull(environment.getProperty("success.message.create.account"));
            }
        }
        return Objects.requireNonNull(environment.getProperty("refusal.message.delay.passed"));
    }

    public String refusal(String message) {
        return Objects.requireNonNull(environment.getProperty("refusal.text")) + " " + message;
    }

    public boolean createPermitTest(Citizen citizen, LocalDate dateTest) {
        if (citizen != null) {
            PermitTest permitTest = new PermitTest();
            permitTest.setDateTest(dateTest);
            permitTest.setDateExpiration(dateTest.plusDays(Integer.parseInt(Objects.requireNonNull(
                    environment.getProperty("permit.dateExpiration.test")))));
            return (saveToDatabase(citizen, permitTest) && generateData(permitTest));
        }
        return false;
    }

    public boolean createPermitVaccin(Citizen citizen, LocalDate dateTest, int nbrDose) {
        if (nbrDose > 0 && nbrDose <= 2 && citizen != null) {
            PermitVaccin permitVaccin =  new PermitVaccin();
            permitVaccin.setDateTest(dateTest);
            if (nbrDose == 1) {
                permitVaccin.setDateExpiration(dateTest.plusDays(Integer.parseInt(Objects.requireNonNull(
                        environment.getProperty("permit.dateExpiration.vaccine.dose1")))));
            } else {
                permitVaccin.setDateExpiration(dateTest.plusDays(Integer.parseInt(Objects.requireNonNull(
                        environment.getProperty("permit.dateExpiration.vaccine.dose2")))));
            }
            permitVaccin.setNbrDose(nbrDose);

            return (saveToDatabase(citizen, permitVaccin) && generateData(permitVaccin));
        }
        return false;
    }

    private boolean saveToDatabase(Citizen citizen, PermitTest permit) {
        if (citizen != null && permit != null) {
            if (citizenRepository.findCitizenByNAS(citizen.getNAS()) != null) {
                PermitTest oldPermit = permitRepository.findByCitizenAndIsActiveIsTrue(citizen);
                oldPermit.setActive(false);
                permitRepository.save(oldPermit);
            }
            permit.setCitizen(citizen);

            citizenRepository.save(citizen);
            citizen.setIdCitizen(citizenRepository.findCitizenByNAS(citizen.getNAS()).getIdCitizen());
            permitRepository.save(permit);
            return true;
        }
        return false;
    }

    public boolean generateData(PermitTest permit) {
        if (permit != null && permit.getCitizen() != null) {
            String filePath = Objects.requireNonNull(environment.getProperty("file.path"))
                    + "/" + permit.getCitizen().getNAS() + "/" + LocalDate.now().toString() + "/" + permit.getCitizen().getNAS();
            String extensionIMG = Objects.requireNonNull(environment.getProperty("file.extension.png"));
            String extensionPDF = Objects.requireNonNull(environment.getProperty("file.extension.pdf"));
            String filePathQR = filePath + extensionIMG;
            String filePathPDF = filePath + extensionPDF;

            try {
                /*return (generateQR(permit, filePathQR) &&
                        generatePDF(filePathPDF, filePathQR));*/
                return (  generateQR(permit, filePathQR) &&
                            generatePDF(filePathPDF, filePathQR) &&
                            sendEmail(permit.getCitizen().getEmail(), filePathPDF, filePathQR));
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean generateQR(PermitTest permit, String filepath) {
        File directory = new File(filepath);
        directory.getParentFile().mkdirs();

        Path path = FileSystems.getDefault().getPath(filepath);
        QRCodeWriter codeWriter = new QRCodeWriter();

        try {
            //QR PNG on directory
            MatrixToImageWriter.writeToPath(codeWriter.encode(permit.toQrData(), BarcodeFormat.QR_CODE,
                    Integer.parseInt(Objects.requireNonNull(environment.getProperty("qrCode.width"))),
                    Integer.parseInt(Objects.requireNonNull(environment.getProperty("qrCode.height")))),
                    Objects.requireNonNull(environment.getProperty("qrCode.extension")), path);

            //QR PNG for database
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BufferedImage qrCode = MatrixToImageWriter.toBufferedImage(qrCodeWriter.encode(permit.toQrData(),
                    BarcodeFormat.QR_CODE,
                    Integer.parseInt(Objects.requireNonNull(environment.getProperty("qrCode.width"))),
                    Integer.parseInt(Objects.requireNonNull(environment.getProperty("qrCode.height")))));
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ImageIO.write(qrCode, "png", stream);
            stream.flush();
            permit.setQrcode(stream.toByteArray());
            permitRepository.save(permit);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean generatePDF(String filepathPDF, String filePathQR) {
        File directory = new File(filepathPDF);
        directory.getParentFile().mkdirs();

        PdfWriter writer;
        try {
            writer = new PdfWriter(filepathPDF);

            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            Image image = new Image(ImageDataFactory.create(filePathQR));
            Paragraph paragraph = new Paragraph("Bonjour toi\n").add("Voici ton code permis santé\n").add(image);

            document.add(paragraph);
            document.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    //no test
    public boolean sendEmail(String mailTo, String filepathPDF, String filePathQR) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(mailTo);
            helper.setSubject(Objects.requireNonNull(environment.getProperty("email.subject.permit")));
            helper.setText(Objects.requireNonNull(environment.getProperty("email.body.permit")));
            helper.addAttachment(Objects.requireNonNull(environment.getProperty("file.attachement.name.QR")) +
                    "." + Objects.requireNonNull(environment.getProperty("file.extension.png")), new File(filePathQR));
            helper.addAttachment(Objects.requireNonNull(environment.getProperty("file.attachement.name.PDF")) +
                    Objects.requireNonNull(environment.getProperty("file.extension.pdf")), new File(filepathPDF));

            mailSender.send(mimeMessage);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //no test
    public boolean sendEmail(String mailTo, String password) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(mailTo);
            helper.setSubject(Objects.requireNonNull(environment.getProperty("email.subject.password")));
            helper.setText(Objects.requireNonNull(environment.getProperty("email.body.password")) + " " + password);

            mailSender.send(mimeMessage);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public PermitTest getPermit(String NAS) {
        Citizen citizen = citizenRepository.findCitizenByNAS(NAS);
        if (citizen != null) {
            return permitRepository.findByCitizenAndIsActiveIsTrue(citizen);
        }
        return null;
    }

    public void getQRCode(int idPermit, HttpServletResponse response)
    {
        response.setContentType("image/png");
        Optional<PermitTest> permit = permitRepository.findById(idPermit);
        if (permit.isPresent()) {
            InputStream is = new ByteArrayInputStream(permit.get().getQrcode());
            try {
                IOUtils.copy(is, response.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * CALL WEBSERVICE MINISTRY
     */

    public Citizen isCitizenValid(Citizen citizen) {
        final String uri = Objects.requireNonNull(environment.getProperty("ramq.address")) + "/ministry?jsonCitizen={jsonCitizen}";
        RestTemplate restTemplate = new RestTemplate();
        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonCitizen = mapper.writeValueAsString(citizen);
            ResponseEntity<Citizen> responseEntity = restTemplate.getForEntity(uri, Citizen.class, jsonCitizen);
            return responseEntity.getBody();

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Citizen getCitizenFromRAMQ(String NAS) {
        final String uri = Objects.requireNonNull(environment.getProperty("ramq.address")) + "/ministry/getCitizen/" + NAS;
        RestTemplate restTemplate = new RestTemplate();
        try {
            String jsonCitizen = restTemplate.getForObject(uri, String.class);
            return new ObjectMapper().setDateFormat(simpleDateFormat).readValue(jsonCitizen, Citizen.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public CredentialsPermit getCredentialsFromRAMQ(String NAS) {
        final String uri = Objects.requireNonNull(environment.getProperty("ramq.address")) + "/ministry/getInfosPermit/" + NAS;
        RestTemplate restTemplate = new RestTemplate();
        try {
            String jsonCredentials = restTemplate.getForObject(uri, String.class);
            if (jsonCredentials != null)
            return new ObjectMapper().setDateFormat(simpleDateFormat).readValue(jsonCredentials, CredentialsPermit.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
}
