package co.wethinkcode.healthsafe;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.jetbrains.annotations.TestOnly;

//import java.net.http.HttpResponse;

import org.junit.jupiter.api.DisplayName;
import org.testng.ITestNGListener;
import org.testng.annotations.Test;
//import org.junit.jupiter.*;
import static org.junit.jupiter.api.Assertions.*;

//Test 1 : ward service starts and exposes wards
//Test 2: Ward service gets it's data from Ingestion service
//Test 3 : Ward service should populate it's own ward and department list
public class WardServiceAppTest {
    @Test
    @DisplayName("GET /wards")
    public void shouldReturnSuccessful() throws Exception {
        WardServiceApp api = new WardServiceApp();
        api.start();

        try {
            HttpResponse<String> response  = (HttpResponse<String>) Unirest.get("http://localhost:7031/wards")
                    .asString();

            assertEquals(200, response.getStatus());
//            assertTrue(response.getBody().contains("\"wardId\":\"W-01\""));
//            assertTrue(response.getBody().contains("\"department\":\"Cardiology\""));
        }finally{
            api.stop();
        }
    }

    @Test
    @DisplayName("GET /wards should return records from Ingestion Service")
    public void shouldReturnCleanWards() throws Exception{
        //1.Start both services
        //IngestionServiceApp ingestionService = new IngestionServiceApp();
        WardServiceApp wardService = new WardServiceApp();
        //ingestionService.start();
        wardService.start();
        //getting wards from WardService(7031)

        try{HttpResponse <String> response = Unirest.get("http://localhost:7031/wards")
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


}
