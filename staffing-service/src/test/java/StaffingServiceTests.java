import co.wethinkcode.healthsafe.StaffingService;
import co.wethinkcode.healthsafe.StaffingServiceApp;

import co.wethinkcode.healthsafe.StaffingResponse;
import co.wethinkcode.healthsafe.WardDto;
//import org.testng.annotations.Test;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class StaffingServiceTests {

    @Test
    public void StaffingShouldReturnWards(){
        //get Wards from ward Service;
        StaffingService staffingService = new StaffingService();
        WardDto wards = staffingService.getWardForSchedule("W-01");

        assertNotNull(wards);
        assertEquals("W-01",wards.getWardId());
        System.out.println(wards);


    }

    @Test
    public void ShouldReturnAlertLevel(){
        StaffingService staffingService = new StaffingService();
       int alertLevel = staffingService.getAlertLevel();

       assertEquals(0,alertLevel);

    }

    @Test
    public void ReturnsWardsAndLevelForSchedule(){
        StaffingService staffingService = new StaffingService();

        StaffingResponse response = staffingService.getStaffingForWard("W-01");

        assertNotNull(response);
        assertEquals("W-01",response.getWardId());
        assertEquals(0,response.getAlertLevel());
    }

    @Test
    public void handleUnknownWard(){
        StaffingService service = new StaffingService();
        assertThrows(RuntimeException.class,
                () -> service.getWardForSchedule("W-99")
        );
    }
}
