
import co.wethinkcode.healthsafe.CsvCleaner;
import co.wethinkcode.healthsafe.IngestionServiceApp;
import co.wethinkcode.healthsafe.WardRecordModel;
import kong.unirest.Unirest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import kong.unirest.HttpResponse;
//import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;


public class IngestionServiceAppTest {
    //ArrayList<WardRecordModel> cleanedRecords = CsvCleaner.getCleanedWards();

    public IngestionServiceAppTest() throws Exception {
    }

    @Test
    @DisplayName("GET /wards")

    public void testIngestionService() throws Exception {
        IngestionServiceApp api = new IngestionServiceApp();
        api.start();
        try {
            HttpResponse<String> response = (HttpResponse<String>) Unirest.get("http://localhost:7030/wards").asString();

            assertEquals(200, response.getStatus());
            assertTrue(response.getBody().contains("\"wardId\":\"W-01\""));
            assertTrue(response.getBody().contains("\"wing\":\"East Wing\""));
            assertTrue(response.getBody().contains("\"department\":\"Cardiology\""));
            assertTrue(response.getBody().contains("\"bedsAvailable\":3"));
        } finally {
            api.stop();
        }
    }


    @Test
    public void testWardToUpperCase(){
        String[] row = {
                "w-01",
                "East Wing",
                "Cardiology",
                "3"
        };
        // get ward id from row,and assert equal
        WardRecordModel wardId = CsvCleaner.cleanRow(row);
        assertEquals("W-01", wardId.getWardId());
    }


    @Test
    public void handlesCasing(){
        String [] row = {
                "W-03",
                "east wing",
                "Cardiology",
                "0"
        };

        WardRecordModel wing = CsvCleaner.cleanRow(row);
        assertEquals("East Wing",wing.getWing());
    }

    @Test
    void normalizeCasing() {
        String[] row = {
                "w-02",
                "west wing",
                "PAEDIATRICS",
                "5"
        };

        WardRecordModel result = CsvCleaner.cleanRow(row);

        assertEquals("Paediatrics", result.getDepartment());
    }

    @Test
    void removeWhitespace() {
        String[] row = {
                " W-01 ",
                "East   Wing ",
                "Cardiology",
                "3"
        };

        WardRecordModel result = CsvCleaner.cleanRow(row);

        assertEquals("W-01", result.getWardId());
        assertEquals("East Wing", result.getWing());
    }

    @Test
    void convertValidBedsToInteger() {
        String[] row = {
                "W-03",
                "East Wing",
                "Cardiology",
                "5"
        };

        WardRecordModel result = CsvCleaner.cleanRow(row);

        assertEquals(5, result.getBedsAvailable());
    }

    @Test
    void rejectNonNumericBeds() {
        String[] row = {
                "W-05",
                "East Wing",
                "Paediatrics",
                "five"
        };

        WardRecordModel result = CsvCleaner.cleanRow(row);

        assertNull(result.getBedsAvailable());
    }



}
