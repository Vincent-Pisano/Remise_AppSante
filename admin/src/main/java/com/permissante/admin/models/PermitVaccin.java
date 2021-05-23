package com.permissante.admin.models;


import com.sun.istack.NotNull;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

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
