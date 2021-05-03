package com.permissante.admin.models;

import lombok.Data;

import javax.persistence.MappedSuperclass;

@Data
@MappedSuperclass
public class User {

    protected String password;

}
