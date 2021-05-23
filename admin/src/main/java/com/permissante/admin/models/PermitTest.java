package com.permissante.admin.models;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE_PERMIS")
@DiscriminatorValue("TEST")
public class PermitTest {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_generator")
    @SequenceGenerator(name = "seq_generator", sequenceName = "permit_seq")
    @Column(name = "PERMIT_ID")
    protected int idPermit;
    @Column(name = "IS_ACTIVE")
    private boolean isActive;
    @Column(name = "DATE_TEST")
    private LocalDate dateTest;
    @Column(name = "DATE_EXPIRATION")
    private LocalDate dateExpiration;
    @OneToOne
    @JoinColumn(name = "CITIZEN_ID")
    protected Citizen citizen;

    @Lob
    @Column(name = "QR_CODE", columnDefinition = "BLOB")
    private byte[] qrcode;

    public PermitTest() {
        isActive = true;
    }

    public String toQrData() {
        return citizen.getNAS() + ";" + dateExpiration + ";" + citizen.getPhoneNbr();
    }

    public boolean isPermitVaccin()
    {
        return this instanceof PermitVaccin;
    }
}