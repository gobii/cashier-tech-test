package com.mikelemos.cashiertechtest.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    private long id;

    @Pattern(regexp = "^(?=\\S+$).{1,}$", message = "The Username must not be blank nor have white spaces!")
    private String username;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[A-Z])(?=\\S+$).{4,}$", message = "The Password must be minimum length of 4 characters, " +
            "at least one upper case letter & number")
    private String password;

    @NotNull(message = "The Date of Birth (dob) must not be blank!")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dob;

    @Pattern(regexp = "[0-9]+", message = "The Payment Card Number must contain only digits!")
    @Size(min = 15, max = 19, message = "The Payment Card Number must be between 15 - 19 characters!")
    private String paymentCardNumber;
}
