package co.wethinkcode.healthsafe;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.util.ArrayList;

public class WardService {
    private static final String INGESTION_SERVICE_URL = "http://localhost:7030/wards";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String getWardsFromIngestionService() {
        HttpResponse<String> response = null;
        try {
            response = Unirest.get(INGESTION_SERVICE_URL)
                    .asString();
            return response.getBody();


        } catch (Exception error) {
            throw new RuntimeException("Ingestion Service is unavailable.");
        }

    }


    public ArrayList<WardServiceResponse> getWardByWardId(String wardId) {
        ArrayList<WardServiceResponse> matchingWards = new ArrayList<>();

        try {
            HttpResponse<String> response = null;
            response = Unirest.get(INGESTION_SERVICE_URL)
                    .asString();

            WardServiceResponse[] wards = objectMapper.readValue(
                    response.getBody(),
                    WardServiceResponse[].class
            );

            for (WardServiceResponse ward : wards) {
                if (ward.getWardId().equalsIgnoreCase(wardId)) {
                    matchingWards.add(ward);
                    break;
                }
            }
            return matchingWards;

        } catch (Exception error) {
            throw new RuntimeException("Could not retrieve wards from Ingestion Service");
        }
    }
}