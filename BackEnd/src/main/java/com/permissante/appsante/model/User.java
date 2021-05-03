package com.permissante.appsante.model;

import lombok.Data;

import javax.persistence.*;

@Data
@MappedSuperclass
public class User {

    protected String password;

}
