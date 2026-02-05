package sn.ods.obi.domain.model.utilisateur;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import sn.ods.obi.domain.model.audit.Auditable;

import java.time.LocalDate;

import java.util.HashSet;

import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "TD_UTILISATEUR")
@SequenceGenerator(name = "seq_user", initialValue = 100, allocationSize = 2, sequenceName = "seq_user")
public  class Utilisateur extends Auditable<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_user")
    @Column(nullable = false, updatable = false, unique = true)
    private Long id;

    @Size(max = 50)
    @Column(name = "user_firstName")
    private String prenom;

    @Size(max = 25)
    @Column(name = "user_lastName", nullable = false)
    private String nom;

    @Size(max = 200)
    @Column(name = "user_pmail")
    private String email;


    @Column(name = "user_password")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    // ignoring password à revoir
    private String password;



    @Column(name = "Uti_FirstLog", columnDefinition = "boolean default true")
    private Boolean firstLog = true;

    @Column(name = "user_status", columnDefinition = "boolean default true")
    private Boolean status;


    @Size(max = 20)
    @Column(name = "user_phoneNumber")
    private String telephone;

    @Size(max = 150)
    @Column(name = "user_adresse")
    private String adresse;

    @Size(max = 10)
    @Column(name = "user_sexe")
    private String sexe;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "TR_USER_PROFILE",  joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "profile_id"))
    private Set<Profile> profiles = new HashSet<>();

    @Size(max = 100)
    @Column(name = "user_lieu_naissance")
    private String lieuDeNaissance;

    @Column(name = "user_date_naissance")
    private LocalDate dateNaissance;

    @Column(name = "org_id")
    private Long organisationId;


}
