package com.flat.backend.geocode.controller;

import com.flat.backend.common.BaseResponseStatus;
import com.flat.backend.common.dto.BaseResponseDto;
import com.flat.backend.geocode.GeocodeKey;
import com.flat.backend.policeStation.repository.PoliceStationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/geocode")
@Slf4j
@RequiredArgsConstructor
public class GeocodeController {

    private final PoliceStationRepository policeRepository;
    private final GeocodeKey geocodeKey;

    @GetMapping("/{query}")
    public ResponseEntity<BaseResponseDto<ResponseEntity<String>>> searchAddress(@PathVariable String query) {
        ByteBuffer buffer = StandardCharsets.UTF_8.encode(query);
        String encode = StandardCharsets.UTF_8.decode(buffer).toString();
        URI uri = UriComponentsBuilder
                .fromUriString("https://naveropenapi.apigw.ntruss.com")
                .path("/map-geocode/v2/geocode")
                .queryParam("query", encode)
                .encode()
                .build()
                .toUri();
        RestTemplate restTemplate = new RestTemplate();
        RequestEntity<Void> request = RequestEntity
                .get(uri)
                .header("X-NCP-APIGW-API-KEY-ID", geocodeKey.getKeyId())
                .header("X-NCP-APIGW-API-KEY", geocodeKey.getKey())
                .build();
        ResponseEntity<String> result = restTemplate.exchange(request, String.class);
        return ResponseEntity
                .ok()
                .body(new BaseResponseDto<>(BaseResponseStatus.OK.getStatusCode(), BaseResponseStatus.OK.getStatusMessage(), result));
    }

    // 경찰서 주소 가져오기
    /*
    @Transactional
    @GetMapping("/police/{query}")
    public void test(@PathVariable String query) {
        ByteBuffer buffer = StandardCharsets.UTF_8.encode(query);
        String encode = StandardCharsets.UTF_8.decode(buffer).toString();
        URI uri = UriComponentsBuilder
                .fromUriString("https://naveropenapi.apigw.ntruss.com")
                .path("/map-geocode/v2/geocode")
                .queryParam("query", encode)
                .encode()
                .build()
                .toUri();
        RestTemplate restTemplate = new RestTemplate();
        RequestEntity<Void> request = RequestEntity
                .get(uri)
                .header("X-NCP-APIGW-API-KEY-ID", "ma90kj4eu7")
                .header("X-NCP-APIGW-API-KEY", "99XkSbZnQwTciMuTbiQ0LDGD9faiUn5Wjp7yAf6o")
                .build();
        ResponseEntity<String> result = restTemplate.exchange(request, String.class);

        JSONParser parser = new JSONParser();
        JSONObject payload = null;
        try {
            payload = (JSONObject) parser.parse(result.getBody());
            JSONArray test = (JSONArray) payload.get("addresses");
            if(test.size() > 0) {
                JSONObject res = (JSONObject) parser.parse(test.get(0).toString());

                PoliceStationLocation policeStationLocation = PoliceStationLocation.builder()
                    .address(query)
                    .lat(res.get("y").toString())
                    .lng(res.get("x").toString())
                    .build();

                policeRepository.save(policeStationLocation);
            }

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    */
}
