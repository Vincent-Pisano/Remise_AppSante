package com.permissante.admin.controller;

import com.permissante.admin.models.Citizen;
import com.permissante.admin.service.AdminService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller; //MVC Controller
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

@Controller
public class AdminController {

    @Autowired
    AdminService service;

    @GetMapping(path = "/")
    public String index() {
        System.out.println("Connection");
        return "index";
    }

    @GetMapping("/citizens")
    public String getAllCitizens(Model model){
        model.addAttribute("listCitizens", service.getAllCitizen());
        return "citizens";
    }

    @GetMapping("/citizens/add")
    public String addCitizen(Model model){
        return "addCitizens";
    }

    @GetMapping("/citizens/edit/{id}")
    public String editCitizen(Model model, @PathVariable(value = "id") Integer id){
        model.addAttribute("citizen", service.getCitizen(id));
        return "editCitizen";
    }

    //remplacez par une désactivation
    @GetMapping("/citizens/delete/{id}")
    public String deleteCitizen(Model model, @PathVariable(value = "id") Integer id){
        service.deactivateCitizen(id);
        return "redirect:/citizens";
    }

    @RequestMapping("/citizen/post")
    public String updateCitizen(Model model,
                               @RequestParam(value = "idCitizen", required = false) Integer idCitizen,
                               @RequestParam(value = "NAS") String NAS,
                               @RequestParam(value = "lastName") String lastName,
                               @RequestParam(value = "firstName") String firstName,
                               @RequestParam(value = "email") String email,
                               @RequestParam(value = "password") String password,
                               @RequestParam(value = "sex") String sex,
                               @RequestParam(value = "birthDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate birthDate,
                               @RequestParam(value = "city") String city,
                               @RequestParam(value = "address") String address,
                               @RequestParam(value = "postalCode") String postalCode,
                               @RequestParam(value = "phoneNbr") String phoneNbr){
        Integer id = service.updateCitizen(
                idCitizen, NAS, lastName, firstName, email, password, sex, birthDate,
                city, address, postalCode, phoneNbr);
        return "redirect:/citizens/editChildList/" + id;
    }

    @GetMapping("/citizens/editChildList/{id}")
    public String editChildList(Model model, @PathVariable(value = "id") Integer id){
        model.addAttribute("citizen", service.getCitizen(id));
        return "editChildCitizen";
    }

    @GetMapping("/citizens/updateChildList")
    public String addChildList(Model model,
                               @RequestParam(value = "NASParent") String NASParent,
                               @RequestParam(value = "NASChild") String NASChild,
                               @RequestParam(value = "isDeleting", required = false) Boolean isDeleting) throws NotFoundException
    {
        Citizen parent = service.updateChildList(NASParent, NASChild, isDeleting);
        model.addAttribute("citizen", parent);
        return "redirect:/citizens/editChildList/" + parent.getIdCitizen();
    }

    @GetMapping("/permits")
    public String getAllPermits(Model model){
        model.addAttribute("listPermis", service.getAllPermit());
        return "permits";
    }

    @GetMapping("/permits/add")
    public String addPermit(Model model){
        model.addAttribute("isVaccin", true);
        return "addPermit";
    }

    @RequestMapping("/permits/post")
    public String updatePermit(Model model,
                               @RequestParam(value = "idPermit", required = false) Integer idPermit,
                               @RequestParam(value = "dateTest") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTest,
                               @RequestParam(value = "dateExpiration") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateExpiration,
                               @RequestParam(value = "nbrDose", required = false) Integer nbrDose,
                               @RequestParam(value = "idCitizen") Integer idCitizen,
                               @RequestParam(value = "isVaccin", required = false) Boolean isVaccin) throws NotFoundException {
        service.updatePermit(idPermit, dateTest, dateExpiration, nbrDose, idCitizen, isVaccin);
        return "redirect:/permits";
    }

    @GetMapping("/permits/edit/{id}")
    public String editPermit(Model model, @PathVariable(value = "id") Integer id){
        model.addAttribute("permit", service.getPermit(id));
        return "editPermit";
    }

    @GetMapping("/permits/delete/{id}")
    public String deletePermit(Model model, @PathVariable(value = "id") Integer id){
        service.deactivatePermit(id);
        return "redirect:/permits";
    }

    @GetMapping("/permits/qrCode/{idPermit}")
    public void getQRCodeByNAS(@PathVariable Integer idPermit, HttpServletResponse response)
    {
        service.getQRCodeByNAS(idPermit, response);
    }
}