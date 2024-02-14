package com.anucode.schoolapp.dto.responseDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentResponseDTO {

    @NotBlank(message = "id should not be null or blank")
    @Positive
    private Long id;

    @NotBlank(message = "firstName should not be null or blank")
    private String firstName;

    @NotBlank(message = "lastName should not be null or blank")
    private String lastName;

    @Past(message = "dateOfBirth is invalid")
    private Date dateOfBirth;

    @NotBlank(message = "address should not be null or blank")
    private String address;
}
