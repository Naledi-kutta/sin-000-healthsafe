package co.wethinkcode.healthsafe;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import kong.unirest.Unirest;
import kong.unirest.HttpResponse;

public class AlertLevelServiceTest {

    @Test
   // @DisplayName("Get /alert-level should return hospital alert level");
    public void getAlertLevelEndpoint() throws Exception {
        AlertLevelServiceApp api = new AlertLevelServiceApp();
        api.start();

        try {
            HttpResponse<String> response = Unirest.get("http/localhost:7032/alert-level")
                    .asString();

            assertEquals(200, response.getStatus());
            assertTrue(response.getBody().contains("\"level\":0"));
        }finally {
            api.stop();
        }
    }

    @Test
    public void getAlertLevel(){
        AlertLevel alertLevel = new AlertLevel();
        assertEquals(0,alertLevel.getAlertLevel());
    }


    @Test
    public void setAlertShouldReturnSuccessful(){
        AlertLevel alertLevel = new AlertLevel();

        alertLevel.setAlertLevel(5);

        assertEquals(5,alertLevel.getAlertLevel());


    }

    @Test
    public void negativeAlertLevelShouldFail(){
        AlertLevel alertLevel = new AlertLevel();
        assertThrows(IllegalArgumentException.class,
                ()-> alertLevel.setAlertLevel(-3));

    }


    @Test
    public void alertAboveEightUnsuccessful(){
        AlertLevel alertLevel = new AlertLevel();

        assertThrows(IllegalArgumentException.class,
                () -> alertLevel.setAlertLevel(9));

    }




}
