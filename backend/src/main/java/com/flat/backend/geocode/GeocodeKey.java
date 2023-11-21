package com.flat.backend.geocode;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class GeocodeKey {

    @Value("${api.keyId}")
    private String keyId;

    @Value("${api.key}")
    private String key;

}
