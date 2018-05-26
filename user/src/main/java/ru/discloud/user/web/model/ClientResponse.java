package ru.discloud.user.web.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.discloud.user.domain.Client;
import ru.discloud.user.domain.Country;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClientResponse {
  private String email;
  private String firstName;
  private String lastName;
  private Country country;
  private String state;
  private String city;
  private String streetAddress;
  private String postalCode;

  public ClientResponse(Client client) {
    this.email = client.getEmail();
    this.firstName = client.getFirstName();
    this.lastName = client.getLastName();
    this.country = client.getCountry();
    this.state = client.getState();
    this.city = client.getCity();
    this.streetAddress = client.getStreetAddress();
    this.postalCode = client.getPostalCode();
  }
}
