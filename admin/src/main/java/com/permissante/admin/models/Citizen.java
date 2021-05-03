package com.permissante.admin.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Citizen extends User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_generator")
    @SequenceGenerator(name = "seq_generator", sequenceName = "citizen_seq")
    @Column(name = "CITIZEN_ID")
    private int idCitizen;

    @Column(name = "SOCIAL_INS_NBR", unique = true)
    private String NAS;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(unique = true)
    private String email;

    private String sex;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @Column(name = "BIRTH_DATE")
    private LocalDate birthDate;

    private String city;

    private String address;

    @Column(name = "POSTAL_CODE")
    private String postalCode;

    @Column(name = "PHONE_NUMBER")
    private String phoneNbr;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "CHILD_PARENT",
            joinColumns = @JoinColumn(name = "ID_PARENT"),
            inverseJoinColumns = @JoinColumn(name = "ID_CHILD"))
    @JoinColumn(name = "NAS")
    private List<Citizen> childList;

    @Column(name = "IS_ACTIVE")
    private boolean isActive;

    public Citizen() {
        isActive = true;
        childList = new ArrayList<>();
    }

}
