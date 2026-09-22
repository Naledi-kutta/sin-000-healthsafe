package co.wethinkcode.healthsafe;

import javax.management.ValueExp;
import java.net.http.HttpResponse;

public class AlertLevel {
    private int alertLevel = 0;

    public AlertLevel(){};

    public int getAlertLevel(){
        return alertLevel;
    };

    public void setAlertLevel(int level){
        if(level < 0 || level > 8){
            throw new IllegalArgumentException("Alert level must be between 0 and 8.");
        }
        this.alertLevel = level;
    }


}
