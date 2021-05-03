package com.permissante.appsante.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@DiscriminatorValue("VACCIN")
public class PermitVaccin extends PermitTest {

    @NotNull
    @Column(name = "NBR_DOSE")
    private int nbrDose;

    public PermitVaccin() {
        super();
    }
}
