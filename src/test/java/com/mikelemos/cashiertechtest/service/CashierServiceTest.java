package com.mikelemos.cashiertechtest.service;

import com.mikelemos.cashiertechtest.entity.User;
import com.mikelemos.cashiertechtest.exception.INNBlockedException;
import com.mikelemos.cashiertechtest.exception.UnderAgeException;
import com.mikelemos.cashiertechtest.exception.UsernameAlreadyRegisteredException;
import com.mikelemos.cashiertechtest.repository.UserRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

import java.time.LocalDate;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CashierServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private Environment environment;

    @InjectMocks
    private CashierService cashierService;

    @SneakyThrows
    @Test
    public void shouldSaveNewUser() {

        when(environment.getProperty(anyString(), eq(String[].class))).thenReturn(new String[]{"111111", "222222"});
        when(userRepository.findByUsername("mikeLemos")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(getValidUserWithId());
        User userCreated = cashierService.registerUser(getValidUser());

        assertThat(userCreated.getId(), is(notNullValue()));
    }

    @SneakyThrows
    @Test
    public void shouldThrowUnderAgeException() {
        assertThrows(UnderAgeException.class, () -> cashierService.registerUser(getInvalidUnderAgeUser()));
    }

    @SneakyThrows
    @Test
    public void shouldThrowUsernameAlreadyRegisteredException() {
        when(userRepository.findByUsername("mikeLemos")).thenReturn(Optional.of(getValidUserWithId()));
        assertThrows(UsernameAlreadyRegisteredException.class, () -> cashierService.registerUser(getValidUser()));
    }

    @SneakyThrows
    @Test
    public void shouldThrowINNBlockedException() {
        when(environment.getProperty(anyString(), eq(String[].class))).thenReturn(new String[]{"111111", "222222", "112233"});
        when(userRepository.findByUsername("mikeLemos")).thenReturn(Optional.empty());
        assertThrows(INNBlockedException.class, () -> cashierService.registerUser(getValidUser()));
    }


    private User getValidUser() {
        return User.builder().username("mikeLemos").dob(LocalDate.parse("1987-01-01")).password("Pass1").paymentCardNumber("1122334455667788").build();
    }

    private User getInvalidUnderAgeUser() {
        User user = getValidUser();
        user.setDob(LocalDate.parse("2010-01-01"));
        return user;
    }

    private User getValidUserWithId() {
        User user = getValidUser();
        user.setId(1);
        return user;
    }
}
