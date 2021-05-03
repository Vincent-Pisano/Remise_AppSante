package com.permissante.appsante.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Admin extends User {

    @Id
    @Column(name = "ADMIN_ID")
    protected String idAdmin;
    private int login;
    private String role;
}
