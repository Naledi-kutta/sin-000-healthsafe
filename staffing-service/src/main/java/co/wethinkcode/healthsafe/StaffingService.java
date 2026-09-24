package co.wethinkcode.healthsafe;

import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.Unirest;
import kong.unirest.HttpResponse;
import kong.unirest.json.JSONObject;

public class StaffingService {
    private static final String WARD_SERVICE_URL = "http://localhost:7031/wards";
    private static final String ALERT_LEVEL_SERVICE_URL = "http://localhost:7032/alert-level";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public StaffingService() {
    }

    public WardDto getWardForSchedule (String wardId) {
        try {
            HttpResponse<String> response = Unirest.get(WARD_SERVICE_URL +"/"+ wardId)
                    .asString();

            if (response.getStatus() == 404) {
                throw new RuntimeException("Unknown Ward");
            }

            if (response.getStatus() != 200) {
                throw new RuntimeException("Ward Service is unavailable"
                );
            }

            WardDto wards[] = objectMapper.readValue(
                    response.getBody(),
                    WardDto[].class
            );

            if (wards.length == 0) {
                throw new RuntimeException("Unknown Ward.");
            }
            return wards[0];

        } catch(RuntimeException error) {
            throw error;
        } catch(Exception error){
        throw new RuntimeException("Could not retrieve wards from Ward Service");
        }
    }


    public int getAlertLevel() {
        try {
            HttpResponse<String> response = Unirest.get(ALERT_LEVEL_SERVICE_URL)
                    .asString();

            if (response.getStatus() != 200) {
                throw new RuntimeException("Alert Level service is unavailable");

            }

            JSONObject json = new JSONObject(response.getBody());
            return json.getInt("level");


        }catch(RuntimeException error){
        throw error;
    } catch (Exception error){
        throw new RuntimeException("Could not retrieve alert level");
        }
    }


    public StaffingResponse getStaffingForWard(String ward_id) {
            WardDto ward = getWardForSchedule(ward_id);
            int alertLevel = getAlertLevel();

            return new StaffingResponse(
                    ward.getWardId(),
                    alertLevel
            );
        }
}

