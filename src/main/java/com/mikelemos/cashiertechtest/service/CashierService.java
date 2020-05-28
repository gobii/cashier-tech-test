package com.mikelemos.cashiertechtest.service;

import com.mikelemos.cashiertechtest.entity.User;
import com.mikelemos.cashiertechtest.exception.INNBlockedException;
import com.mikelemos.cashiertechtest.exception.UnderAgeException;
import com.mikelemos.cashiertechtest.exception.UsernameAlreadyRegisteredException;
import com.mikelemos.cashiertechtest.repository.UserRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.stream.Stream;

@Service
public class CashierService {

    private UserRepository userRepository;
    private Environment env;

    @Autowired
    public CashierService(UserRepository userRepository, Environment env) {
        this.userRepository = userRepository;
        this.env = env;
    }

    public User registerUser(@NonNull User user) throws UnderAgeException, UsernameAlreadyRegisteredException, INNBlockedException {

        validateUserProperties(user);

        return userRepository.save(user);
    }

    private void validateUserProperties(User user) throws UnderAgeException, UsernameAlreadyRegisteredException, INNBlockedException {
        if (isUnderAge(user.getDob())) {
            throw new UnderAgeException();
        } else if (isUsernameAlreadyRegistered(user.getUsername())) {
            throw new UsernameAlreadyRegisteredException();
        } else if (isINNBlocked(user.getPaymentCardNumber())) {
            throw new INNBlockedException();
        }
    }

    private boolean isINNBlocked(String paymentCardNumber) {
        return Stream.of(env.getProperty("inn.blocked.list", String[].class)).anyMatch(innBlocked -> paymentCardNumber.startsWith(innBlocked));
    }

    private boolean isUsernameAlreadyRegistered(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    private boolean isUnderAge(LocalDate dob) {
        return Period.between(dob, LocalDate.now()).getYears() < 18;
    }
}
