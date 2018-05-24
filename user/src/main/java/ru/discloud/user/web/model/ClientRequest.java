package ru.discloud.user.web.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class ClientRequest {
    @Email(message = "Provided email isn't valid")
    private String email;

    @NotBlank(message = "Property 'firstName' isn't provided")
    private String firstName;

    @NotBlank(message = "Property 'lastName' isn't provided")
    private String lastName;

    @NotBlank(message = "Property 'country' isn't provided")
    private String country;

    private String state;

    @NotBlank(message = "Property 'city' isn't provided")
    private String city;

    @NotBlank(message = "Property 'streetAddress' isn't provided")
    private String streetAddress;

    @Pattern(regexp = "(\\d{6})", message = "Property 'postalCode' isn't provided")
    private String postalCode;
}
