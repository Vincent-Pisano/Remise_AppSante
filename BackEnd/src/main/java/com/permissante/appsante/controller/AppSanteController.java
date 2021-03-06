package com.permissante.appsante.controller;

import com.permissante.appsante.model.Citizen;
import com.permissante.appsante.model.CredentialsPermit;
import com.permissante.appsante.model.PermitTest;
import com.permissante.appsante.services.AppSanteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin("http://localhost:5178")
public class AppSanteController {

    @Autowired
    private AppSanteService service;

    @GetMapping("/appSante/login/{email}/{password}")
    public Citizen login(@PathVariable("email") String email, @PathVariable("password") String password) {
        return service.login(email, password);
    }

    @PostMapping("/appSante/isCitizenValid")
    public Citizen isCitizenValid(@RequestBody Citizen citizen) {
        return service.isCitizenValid(citizen);
    }

    @PostMapping("/appSante/renewPermit")
    public String renewPermit(@RequestBody Citizen citizen) {
        return service.renewPermit(citizen);
    }

    @PostMapping("/appSante/createAccount")
    public String createAccount(@RequestBody Citizen citizen) {
        return service.createAccount(citizen);
    }

    @PostMapping("/appSante/createAccountMinor")
    public String createAccountMinor(@RequestBody Citizen[] citizens) {
        return service.createAccount(citizens[0], citizens[1]);
    }

    @GetMapping("/appSante/getInfosPermit/{NAS}")
    public PermitTest getPermit(@PathVariable("NAS") String NAS) {
        return service.getPermit(NAS);
    }

    @GetMapping("/appSante/getForgotPassword/{email}")
    public boolean getForgotPassword(@PathVariable("email") String email) {
        return service.getForgotPassword(email);
    }

    @GetMapping("/appSante/qrCode/{idPermit}")
    public void getQRCode(@PathVariable int idPermit, HttpServletResponse response)
    {
        service.getQRCode(idPermit, response);
    }

    /*
        Unused methods
     */

    @GetMapping("/appSante/getCredentialsPermit/{NAS}")
    public CredentialsPermit getPermitFromRAMQ(@PathVariable("NAS") String NAS) {
        return service.getCredentialsFromRAMQ(NAS);
    }

    @GetMapping("/appSante/getCitizen")
    public Citizen getCitizen(@RequestParam String NAS) {
        return service.getCitizenFromRAMQ(NAS);
    }
}
