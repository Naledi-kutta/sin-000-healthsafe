package co.wethinkcode.healthsafe;

import io.javalin.Javalin;
import org.json.JSONObject;

import java.util.Map;

public class AlertLevelServiceApp {

    private final Javalin server;
    private final AlertLevel alertLevel;

    public AlertLevelServiceApp() throws Exception {
        this.alertLevel = new AlertLevel();
        this.server = Javalin.create();
        this.server.get("/health",ctx -> ctx.result("OK"));
        this.server.get("/alert-level",ctx -> {
            ctx.json(Map.of("level",alertLevel.getAlertLevel()));
        });

//        this.server.get("/alert-level",ctx -> {
//            JSONObject response = new JSONObject();
//
//            response.put("level",alertLevel.getAlertLevel()
//            );
//            ctx.json(response);
//
//        });
    }

    public Javalin start(){
        return this.server.start(7032);
    }

    public Javalin stop(){
        return this.server.stop();
    }



    public static void main(String[] args) throws Exception {
        AlertLevelServiceApp app = new AlertLevelServiceApp();
        app.start();


        // TODO (Tracks the hospital Emergency Status (0-8, 8 = full Code Blue).)
        // Add domain endpoints for alert-level-service here.
    }
}
