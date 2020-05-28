package com.mikelemos.cashiertechtest.controller;

import com.mikelemos.cashiertechtest.entity.User;
import com.mikelemos.cashiertechtest.exception.INNBlockedException;
import com.mikelemos.cashiertechtest.exception.UnderAgeException;
import com.mikelemos.cashiertechtest.exception.UsernameAlreadyRegisteredException;
import com.mikelemos.cashiertechtest.service.CashierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
public class CashierController {

    private CashierService cashierService;

    @Autowired
    public CashierController(CashierService cashierService) {
        this.cashierService = cashierService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody User user) {

        User createdUser;

        try {
            createdUser = cashierService.registerUser(user);
        } catch (UnderAgeException e) {
            throw new ResponseStatusException((HttpStatus.FORBIDDEN), "User cannot be under 18 years old!", e);
        } catch (UsernameAlreadyRegisteredException e) {
            throw new ResponseStatusException((HttpStatus.CONFLICT), "Username already registered!", e);
        } catch (INNBlockedException e) {
            throw new ResponseStatusException((HttpStatus.NOT_ACCEPTABLE), "INN for the provided payment card number is blocked!", e);
        }

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdUser.getId())
                .toUri();

        return ResponseEntity.created(uri)
                .body(createdUser);
    }
}
