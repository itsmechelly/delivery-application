package com.deliveryapp.service;

import com.deliveryapp.model.domain.Address;
import com.deliveryapp.repository.AddressRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
//@Scope("prototype")
public class AddressServiceImpl implements AddressService {

    @Value("${geo.api.fy.key}")
    private String GEO_API_KEY;

    private final AddressRepository addressRepository;
    private static final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public Address resolveAddress(String searchTerm) throws IOException, InterruptedException {
        if (StringUtils.isNoneEmpty(searchTerm)) {
            log.info("addressService.resolveAddress: going to encode String data searchTerm={} for HttpClient object", searchTerm);
            String queryParams = UriUtils.encodeQuery(searchTerm, StandardCharsets.UTF_8);
            HttpClient client = HttpClient.newHttpClient();

            log.info("addressService.resolveAddress: going to prepare HttpRequest & HttpResponse objects for 'geoapify' external source");
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.geoapify.com/v1/geocode/search?text=" + queryParams + "&format=json&apiKey=" + GEO_API_KEY))
                    .header("Content-Type", "application/json")
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            log.info("addressService.resolveAddress: going to prepare JSONObject object to fetch address data from 'geoapify' external source");
            JSONObject object = new JSONObject(response.body());
            JSONObject jsonObject = ((JSONObject) (((JSONArray) object.get("results")).get(0)));

            log.info("addressService.resolveAddress: resolving JSONObject object to structured Address model object, using jsonObject={}", jsonObject);
            return Address.builder()
                    .line1(jsonObject.getString("address_line1"))
                    .line2(jsonObject.getString("address_line2"))
                    .country(jsonObject.getString("country"))
                    .region(jsonObject.getString("state"))
                    .city(jsonObject.getString("city"))
                    .street(jsonObject.getString("street"))
                    .homeNum(jsonObject.getString("housenumber"))
                    .postcode(jsonObject.getString("postcode")).build();
        } else {
            log.error("addressService.resolveAddress: empty or null value for searchTerm, searchTerm={}", searchTerm);
            throw new NullPointerException("Something went wrong, please try again.");
        }
    }
}
