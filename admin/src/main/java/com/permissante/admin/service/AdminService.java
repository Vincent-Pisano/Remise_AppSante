package com.permissante.admin.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.qrcode.QRCodeWriter;
import com.permissante.admin.models.Citizen;
import com.permissante.admin.models.PermitTest;
import com.permissante.admin.models.PermitVaccin;
import com.permissante.admin.repositories.CitizenRepository;
import com.permissante.admin.repositories.PermitRepository;
import javassist.NotFoundException;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    CitizenRepository citizenRepository;

    @Autowired
    PermitRepository permitRepository;

    @Autowired
    Environment environment;

    public Integer updateCitizen(Integer idCitizen, String NAS, String lastName, String firstName,
                              String email, String password, String sex, LocalDate birthDate, String city, String address,
                              String postalCode, String phoneNbr) {
        Citizen citizen;
        if (idCitizen != null)
            citizen = citizenRepository.getOne(idCitizen);
        else
            citizen = new Citizen();
        citizen.setNAS(NAS);
        citizen.setLastName(lastName);
        citizen.setFirstName(firstName);
        citizen.setEmail(email);
        citizen.setPassword(password);
        citizen.setSex(sex);
        citizen.setBirthDate(birthDate);
        citizen.setCity(city);
        citizen.setAddress(address);
        citizen.setPostalCode(postalCode);
        citizen.setPhoneNbr(phoneNbr);
        return citizenRepository.save(citizen).getIdCitizen();
    }

    public Citizen updateChildList(String NASParent, String NASChild, Boolean isDeleting) throws NotFoundException {
        Citizen parent = citizenRepository.findCitizenByNAS(NASParent);
        Citizen child = citizenRepository.findCitizenByNAS(NASChild);
        if (child != null)
        {
            if (isDeleting != null)
                parent.getChildList().remove(child);
            else
            {
                if (!parent.getChildList().contains(child))
                {
                    parent.getChildList().add(child);
                }

            }
        }
        else
            throw new NotFoundException("Citizen with NAS " + NASChild + " doesn't exist");
        return citizenRepository.save(parent);
    }

    public PermitTest updatePermit(Integer idPermit, LocalDate dateTest, LocalDate dateExpiration, Integer nbrDose,
                             Integer idCitizen, Boolean isVaccin) throws NotFoundException {
        PermitTest permit = createPermit(idPermit, nbrDose, isVaccin);
        fillPermitInfos(dateTest, dateExpiration, idCitizen, permit);
        return permit;
    }

    private PermitTest createPermit(Integer idPermit, Integer nbrDose, Boolean isVaccin) {
        if (isVaccin != null)
        {
            PermitVaccin permitVaccin = idPermit != null ? (PermitVaccin) permitRepository.getOne(idPermit) : new PermitVaccin();
            permitVaccin.setNbrDose(nbrDose);
            return permitVaccin;
        }
        else {
            return idPermit != null ? permitRepository.getOne(idPermit) : new PermitTest();
        }
    }

    private void fillPermitInfos(LocalDate dateTest, LocalDate dateExpiration, Integer idCitizen,
                                 PermitTest permit) throws NotFoundException {
        permit.setDateTest(dateTest);
        permit.setDateExpiration(dateExpiration);

        Optional<Citizen> citizen = citizenRepository.findById(idCitizen);
        if (citizen.isPresent())
            permit.setCitizen(citizen.get());
        else
            throw new NotFoundException("Citizen with id " + idCitizen + " doesn't exist");

        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BufferedImage qrCode = MatrixToImageWriter.toBufferedImage(qrCodeWriter.encode(permit.toQrData(),
                    BarcodeFormat.QR_CODE,
                    Integer.parseInt(Objects.requireNonNull(environment.getProperty("qrCode.width"))),
                    Integer.parseInt(Objects.requireNonNull(environment.getProperty("qrCode.height")))));
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ImageIO.write(qrCode, "png", stream);
            stream.flush();
            permit.setQrcode(stream.toByteArray());
        } catch (IOException | WriterException e) {
            e.printStackTrace();
        }
        permitRepository.save(permit);
    }

    public void getQRCodeByNAS(Integer idPermit, HttpServletResponse response) {
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

    public void deactivateCitizen(Integer id) {
        Citizen citizen = citizenRepository.getOne(id);
        PermitTest permit = permitRepository.findByCitizenAndIsActiveIsTrue(citizen);
        if (permit != null)
        {
            permit.setActive(false);
            permitRepository.save(permit);
        }
        citizen.setActive(false);
        citizenRepository.save(citizen);
    }

    public List<Citizen> getAllCitizen()
    {
        return citizenRepository.findAll();
    }

    public Optional<Citizen> getCitizen(Integer idCitizen)
    {
        return citizenRepository.findById(idCitizen);
    }

    public void deactivatePermit(Integer idPermit)
    {
        PermitTest permitTest = permitRepository.getOne(idPermit);
        permitTest.setActive(false);
        permitRepository.save(permitTest);
    }

    public Optional<PermitTest> getPermit(Integer idPermit)
    {
        return permitRepository.findById(idPermit);
    }

    public List<PermitTest> getAllPermit()
    {
        return permitRepository.findAll();
    }

}
