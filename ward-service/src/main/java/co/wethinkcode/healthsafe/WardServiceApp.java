package co.wethinkcode.healthsafe;

import co.wethinkcode.healthsafe.mq.EquipmentAlertPublisher;
import co.wethinkcode.healthsafe.mq.MqConfig;
import io.javalin.Javalin;
import java.util.ArrayList;
import java.util.Map;
import javax.jms.JMSException;


public class WardServiceApp {
   // private static final String INGESTION_SERVICE_URL = "http://localhost:7030/wards";

    private final Javalin server;
    private final MqConfig mqConfig = new MqConfig();
    private final EquipmentAlertPublisher equipmentAlertPublisher = new EquipmentAlertPublisher();


    public WardServiceApp(){
        this.server = Javalin.create();
        this.server.get("/wards",ctx -> ctx.json(new WardService().getWardsFromIngestionService()));
//        this.server.get("/wards/{id}", ctx ->
//                ctx.json(new WardService().getWardByWardId(ctx.pathParam("id"))));
        this.server.get("/wards/{id}",ctx -> {
            ArrayList<WardServiceResponse> validWard = new WardService()
                    .getWardByWardId(ctx.pathParam("id"));
            if(validWard.isEmpty()){
                ctx.status(404);
                ctx.result("Unkown ward: " + ctx.pathParam("id"));
                return;
            }
            ctx.json(validWard);
        });

        this.server.exception((RuntimeException.class), (e,ctx) -> {
            e.printStackTrace();
            ctx.status(500);
            ctx.result(e.getMessage());
        });
        //mqConfig.startListening();

        this.server.post("/wards/{id}/equipment-failure", ctx -> {
            String wardId = ctx.pathParam("id");
            Map<String, Object> body = ctx.bodyAsClass(Map.class);
            String equipment = (String) body.getOrDefault("equipment", "unspecified");
            try {
                equipmentAlertPublisher.sendAlert(wardId, equipment);
                ctx.status(202);
                ctx.result("Equipment failure alert queued for ward " + wardId);
            } catch (JMSException e) {
                ctx.status(502);
                ctx.result("Could not queue equipment failure alert: " + e.getMessage());
            }
        });

    }

    public Javalin start(){
        Javalin app = this.server.start(7031);
        mqConfig.startListening();
        return app;
    }

    public Javalin stop(){
        return this.server.stop();
    }


    public static void main(String[] args) {
        WardServiceApp api = new WardServiceApp();
        api.start();

//        Javalin app = Javalin.create().start(7031);
//        app.get("/health", ctx -> ctx.result("OK"));


        // TODO (Provides lists of wards and departments.)
        // Add domain endpoints for ward-service here.
    }
}

// MQ TODO: subscribes to ActiveMQ topic MqConfig.TOPIC at MqConfig.BROKER_URL (see co.wethinkcode.healthsafe.mq.MqConfig)
// MQ TODO: publishes to ActiveMQ queue MqConfig.QUEUE when it detects an equipment failure on one of its wards.
