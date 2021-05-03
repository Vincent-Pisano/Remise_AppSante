package ministry.ramq.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
public class Citizen {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_generator")
    @SequenceGenerator(name = "seq_generator", sequenceName = "citizen_seq")
    @Column(name = "CITIZEN_ID")
    protected int idCitizen;
    @NotNull
    @Column(name = "SOCIAL_INS_NBR", unique = true)
    protected String NAS;
    @NotNull
    @Column(name = "LAST_NAME")
    protected String lastName;
    @NotNull
    @Column(name = "FIRST_NAME")
    protected String firstName;
    @NotNull
    @Column(unique = true)
    protected String email;
    @NotNull
    protected String password;
    @NotNull
    protected String sex;
    @NotNull
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @Column(name = "BIRTH_DATE")
    protected LocalDate birthDate;
    @NotNull
    protected String city;
    @NotNull
    protected String address;
    @NotNull
    @Column(name = "POSTAL_CODE")
    protected String postalCode;
    @NotNull
    @Column(name = "PHONE_NUMBER")
    protected String phoneNbr;

    @ManyToMany
    @JoinTable(
            name = "CHILD_PARENT",
            joinColumns = @JoinColumn(name = "ID_PARENT"),
            inverseJoinColumns = @JoinColumn(name = "ID_CHILD"))
    @JoinColumn(name = "NAS")
    private List<Citizen> childList;

    @NotNull
    @Column(name = "IS_ACTIVE")
    protected boolean isActive;

    public Citizen() {
        isActive = true;
    }
}
