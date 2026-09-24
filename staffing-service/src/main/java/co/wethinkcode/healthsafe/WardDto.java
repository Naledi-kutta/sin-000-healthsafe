package co.wethinkcode.healthsafe;

public class WardDto {
    private String wardId;
    private String wing;
    private String department;
    private Integer bedsAvailable;


    public WardDto(){};

    public String getWardId() {
        return wardId;
    }

    public String getWing() {
        return wing;
    }

    public String getDepartment() {
        return department;
    }

    public Integer getBedsAvailable() {
        return bedsAvailable;
    }



}
