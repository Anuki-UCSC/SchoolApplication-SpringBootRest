package com.anucode.schoolapp.dto.requestDto;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;


import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentRequestDTO {

    @NotBlank(message = "firstName should not be null or blank")
    private String firstName;

    @NotBlank(message = "lastName should not be null or blank")
    private String lastName;

    @Past(message = "dateOfBirth is invalid")
    private Date dateOfBirth;

    @NotBlank(message = "address should not be null or blank")
    private String address;
}
