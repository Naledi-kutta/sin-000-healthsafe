package co.wethinkcode.healthsafe;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.jetbrains.annotations.TestOnly;

//import java.net.http.HttpResponse;

import org.junit.jupiter.api.DisplayName;
import org.testng.ITestNGListener;
import org.testng.annotations.Test;
//import org.junit.jupiter.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

//Test 1 : ward service starts and exposes wards
//Test 2: Ward service gets it's data from Ingestion service
//Test 3 : Ward service should populate it's own ward and department list
public class WardServiceAppTest {

    private static String WARD_SERVICE_URL = "http://localhost:7031/wards";
   private WardServiceApp wardService = new WardServiceApp();

    @Test
    @DisplayName("GET /wards")
    public void shouldReturnSuccessful() throws Exception {
        //WardServiceApp api = new WardServiceApp();
        wardService.start();

        try {
            HttpResponse<String> response  = Unirest.get(WARD_SERVICE_URL)
                    .asString();

            assertEquals(200, response.getStatus());
//            assertTrue(response.getBody().contains("\"wardId\":\"W-01\""));
//            assertTrue(response.getBody().contains("\"department\":\"Cardiology\""));
        }finally{
            wardService.stop();
        }
    }

    @Test
    @DisplayName("GET /wards should return records from Ingestion Service")
    public void shouldReturnCleanWards() throws Exception{
        //1.Start both services
        //IngestionServiceApp ingestionService = new IngestionServiceApp();
       // WardServiceApp wardService = new WardServiceApp();
        //ingestionService.start();
        wardService.start();
        //getting wards from WardService(7031)

        try{HttpResponse <String> response = Unirest.get(WARD_SERVICE_URL)
                .asString();
            assertEquals(200,response.getStatus());
            System.out.println(response.toString());
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
        //if Ingestion is unavailable,should return error 500 "Error connecting to server"
        try {
            //WardServiceApp wardService = new WardServiceApp();
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

        //WardServiceApp wardService = new WardServiceApp();
        //create a new ward service app
        wardService.start();

        try {
            //receive a json response from ward service with cleaned records
            HttpResponse<String> response =
                    Unirest.get("http://localhost:7031/wards/W-01")
                            .asString();

            //call getWardById on response
            java.util.ArrayList<WardResponse> ward = new WardService().getWardByWardId("W-01");


            assertEquals(200, response.getStatus());
            assertEquals("\"wardId\":\"W-01\"",ward);
           // assertEquals(response.getBody().contains());
//            assertTrue(response.getBody().contains("\"wardId\":\"W-01\""));
//            assertTrue(response.getBody().contains("\"wing\":\"East Wing\""));
//            assertTrue(response.getBody().contains("\"department\":\"Cardiology\""));
//            assertTrue(response.getBody().contains("\"bedsAvailable\":3"));


        } finally {
            wardService.stop();
        }
    }

    @Test
    @DisplayName("GET /wards/{id} should return 404 for an unknown ward")
    void shouldReturn404ForUnknownWard() throws Exception {

        //WardServiceApp wardService = new WardServiceApp();
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
