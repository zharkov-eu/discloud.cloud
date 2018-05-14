package ru.discloud.user.web.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class ClientRequest {
    @Email(message = "Provided email isn't valid")
    private String email;
    @NotEmpty(message = "Property 'firstName' isn't provided")
    private String firstName;
    @NotEmpty(message = "Property 'lastName' isn't provided")
    private String lastName;
    @NotEmpty(message = "Property 'country' isn't provided")
    private String country;
    private String state;
    @NotEmpty(message = "Property 'city' isn't provided")
    private String city;
    @NotEmpty(message = "Property 'streetAddress' isn't provided")
    private String streetAddress;
    @NotEmpty(message = "Property 'postalCode' isn't provided")
    private String postalCode;
}
