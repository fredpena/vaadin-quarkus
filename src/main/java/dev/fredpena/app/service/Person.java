package dev.fredpena.app.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author me@fredpena.dev
 * @created 24/01/2024  - 11:35
 */
@Getter
@Setter
@Entity
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Person implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long code;

    @NotNull(message = "This field can not be blank")
    @Size(message = "The size must be between 1 and 50", min = 1, max = 50)
    private String firstName;

    @JsonIgnore
    @NotNull(message = "This field can not be blank")
    @Size(message = "The size must be between 1 and 50", min = 1, max = 50)
    private String lastName;

    @Size(message = "The maximum size is 50", max = 50)
    private String email;

    @NotNull(message = "This field can not be blank")
    @Size(message = "The size must be between 1 and 50", min = 1, max = 50)
    private String phone;

    @NotNull(message = "This field can not be blank.")
    private LocalDate dateOfBirth;

    @Size(message = "The maximum size is 100", max = 100)
    private String occupation;

    @Size(message = "The maximum size is 100", max = 100)
    private String role;

    private boolean important;

}