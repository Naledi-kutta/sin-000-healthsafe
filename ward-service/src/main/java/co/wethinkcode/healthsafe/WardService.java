package co.wethinkcode.healthsafe;

import kong.unirest.HttpResponse;
import kong.unirest.ObjectMapper;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import com.fasterxml.jackson.databind.ObjectMapper.*;


import java.nio.channels.NetworkChannel;
import java.util.ArrayList;
import java.util.HashMap;

//returns wardIds and departments for WardServiceApp to ingest
public class WardService {
    private static final String INGESTION_SERVICE_URL = "http://localhost:7030/wards";
    private final com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();

    public String getWardsFromIngestionService() {
        //calls Ingestion Service's /wards endpoint and returns wards
        HttpResponse<String> response = null;
        try {
            response = Unirest.get(INGESTION_SERVICE_URL)
                    .asString();
            return response.getBody();
            //if Ingestion service is offline, output error

        } catch (Exception error) {
            throw new RuntimeException("Ingestion Service is unavailable.");
        }

    }

    //method that lists a specific ward when given a ward ID
    public ArrayList<WardResponse> getWardByWardId(String wardId) {
        //access WardRecordModel
        ArrayList<WardResponse> matchingWards = new ArrayList<>();

        try {
            //convert response from ingestion service into java objects(WardRecordModel)
            HttpResponse<String> response = null;
            response = Unirest.get(INGESTION_SERVICE_URL)
                    .asString();

//            HttpResponse<String> jsonString = response;
//            WardResponse[] wards = objectMapper.readValue(
//                    jsonString.getBody(),
//                    WardResponse[].class
//            );

            WardResponse[] wards = objectMapper.readValue(
                    response.getBody(),
                    WardResponse[].class
            );

            for (WardResponse ward : wards) {
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