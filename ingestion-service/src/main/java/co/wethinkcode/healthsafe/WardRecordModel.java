package co.wethinkcode.healthsafe;

import com.opencsv.bean.CsvBindAndJoinByName;
import com.opencsv.bean.CsvBindByName;

public class WardRecordModel {
    @CsvBindByName(column = "ward_Id")
    private String wardId;

    @CsvBindByName(column = "Wing")
    private String wing;

    @CsvBindByName(column = "department")
    private String department;

    @CsvBindByName(column = "beds_available")
    Integer bedsAvailable;


    private String notes;
    

    public WardRecordModel(String wardId, String wing, String department, Integer bedsAvailable, String notes) {
        this.wardId = wardId;
        this.wing = wing;
        this.department = department;
        this.bedsAvailable = bedsAvailable;
        this.notes = notes;
    }

    public WardRecordModel(){};

//    public WardRecordModel(String wardId, String wing, String department, Integer beds, String notes) {
//    }


    public String getWardId(){
        return wardId;
    }

    public String getWing(){
        return wing;
    }

    public String getDepartment(){
        return department;
    }

    public Integer getBedsAvailable(){
        return bedsAvailable;
    }

}
