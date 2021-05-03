package ministry.ramq.model;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Entity
public class CitizenInfosCovid {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_generator")
    @SequenceGenerator(name = "seq_generator", sequenceName = "citizen_infos_covid_seq")
    private int idCitizenInfosCovid;

    @NotNull
    @OneToOne
    @JoinColumn(name = "CITIZEN_ID")
    private Citizen citizen;

    @NotNull
    private String type;

    private String results;

    private String nbrDose;

    @NotNull
    private LocalDate dateTest;

    @NotNull
    private boolean isCurrent;

    public CitizenInfosCovid() {
        isCurrent = true;
    }
}
