package co.wethinkcode.healthsafe;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
//import java.net.http.HttpResponse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;


public class WardServiceAppTest {

    private static String WARD_SERVICE_URL = "http://localhost:7031/wards";
    private WardServiceApp wardService;


    @BeforeEach
    void setUp(){
        wardService = new WardServiceApp();
    }

    @AfterEach
    void tearDown(){
        wardService.stop();
    }


    @Test
    @DisplayName("GET /wards")
    public void shouldReturnSuccessful() throws Exception {
        wardService.start();
        try {
            HttpResponse<String> response = Unirest.get(WARD_SERVICE_URL)
                    .asString();

            assertEquals(200, response.getStatus());
            assertTrue(response.getBody().contains("\"wardId\":\"W-01\""));
            assertTrue(response.getBody().contains("\"department\":\"Cardiology\""));
        }finally {
            wardService.stop();
        }
    }

    @Test
    @DisplayName("GET /wards should return records from Ingestion Service")
    public void shouldReturnCleanWards() throws Exception{
       // IngestionServiceApp ingestionService = new IngestionServiceApp();

        wardService.start();
        //getting wards from WardService(7031)

        try{HttpResponse <String> response = Unirest.get(WARD_SERVICE_URL)
                .asString();
            assertEquals(200,response.getStatus());
            assertTrue(response.getBody().contains("\"wardId\":\"W-01\""));
            assertTrue(response.getBody().contains("\"wing\":\"East Wing\""));
            assertTrue(response.getBody().contains("\"department\":\"Cardiology\""));
            assertTrue(response.getBody().contains("\"bedsAvailable\":3"));

    } finally{
            //ingestionService.stop();
            wardService.stop();
        }

    }

    @Test
    @DisplayName("Get /wards should handle when Ingestion Service is unvailable")
    public void shouldHandleUnavailableIngestionService() throws Exception{
        try {
            wardService.start();

            HttpResponse<String> response = Unirest.get(WARD_SERVICE_URL)
                    .asString();

            assertEquals(500, response.getStatus());
        }
        finally {
            wardService.stop();
        }
    }


    @Test
    @DisplayName("GET /wards/{id} should return a specific ward")
    void shouldReturnSpecificWard() throws Exception {
        wardService.start();

        try {
            HttpResponse<String> response =
                    Unirest.get("http://localhost:7031/wards/W-01")
                            .asString();
            java.util.ArrayList<WardServiceResponse> ward = new WardService().getWardByWardId("W-01");


            assertEquals(200, response.getStatus());
            assertTrue(response.getBody().contains("\"wardId\":\"W-01\""));
            assertTrue(response.getBody().contains("\"wing\":\"East Wing\""));
            assertTrue(response.getBody().contains("\"department\":\"Cardiology\""));
            assertTrue(response.getBody().contains("\"bedsAvailable\":3"));


        } finally {
            wardService.stop();
        }
    }

    @Test
    @DisplayName("GET /wards/{id} should return 404 for an unknown ward")
    void shouldReturn404ForUnknownWard() throws Exception {
        wardService.start();

        try {
            HttpResponse<String> response =
                    Unirest.get("http://localhost:7031/wards/W-999")
                            .asString();

            assertEquals(404, response.getStatus());

        } finally {
            wardService.stop();
        }
    }


}
