package ru.discloud.user.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.discloud.user.service.CountryService;
import ru.discloud.user.web.model.CountryResponse;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user/country")
public class CountryController {
  private final CountryService countryService;

  @Autowired
  public CountryController(CountryService countryService) {
    this.countryService = countryService;
  }

  @RequestMapping(method = RequestMethod.GET)
  public List<CountryResponse> getCountries() {
    return countryService.findAll()
        .stream()
        .map(CountryResponse::new)
        .collect(Collectors.toList());
  }

  @RequestMapping(value = "/{key}", method = RequestMethod.GET)
  public CountryResponse getCountry(@PathVariable String key) {
    return new CountryResponse(countryService.findByKey(key));
  }
}