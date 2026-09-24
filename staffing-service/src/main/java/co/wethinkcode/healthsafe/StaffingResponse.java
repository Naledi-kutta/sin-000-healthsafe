package co.wethinkcode.healthsafe;
//This class is used for returning json strings and parsing them into java objects
public class StaffingResponse {
    private int alertLevel;
    private String wardId;
    private String department;

    public StaffingResponse(){};

    public StaffingResponse(String wardId, int alertLevel) {
        this.wardId = wardId;
        this.alertLevel = alertLevel;
    }


    public StaffingResponse(int level, String wardId, String department){
        this.alertLevel = level;
        this.wardId = wardId;
        this.department = department;
    }
//
//    public StaffingResponse(String ward){
//        this.wardId = ward;
//    }
//
//    public StaffingResponse(int level){
//        this.alertLevel = level;
//    }
//

    public int getAlertLevel() {
        return alertLevel;
    }

    public String getWardId() {
        return wardId;
    }

    public String getDepartment(){
       return department;
    }
}
