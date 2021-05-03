package com.permissante.admin.controller;

import com.permissante.admin.repositories.CitizenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller; //MVC Controller
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @Autowired
    CitizenRepository citizenRepository;

    @GetMapping(path = "/")
    public String index() {
        System.out.println("Connection");
        return "index";
    }

    @GetMapping("/citizens")
    public String getAllCitizens(Model model){
        model.addAttribute("listCitizens", citizenRepository.findAll());
        return "citizens";
    }
}
