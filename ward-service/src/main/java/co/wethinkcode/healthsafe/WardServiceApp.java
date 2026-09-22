package co.wethinkcode.healthsafe;

import io.javalin.Javalin;
import kong.unirest.Unirest;
import kong.unirest.HttpResponse;
//import java.net.http.HttpResponse;
import java.rmi.server.UnicastRemoteObject;


public class WardServiceApp {
   // private static final String INGESTION_SERVICE_URL = "http://localhost:7030/wards";

    private final Javalin server;
    //object where data from Ingestion will be populated

    public WardServiceApp(){
        this.server = Javalin.create();
        this.server.get("/wards",ctx -> ctx.json(new WardService().getWardsFromIngestionService()));
        this.server.get("/wards/{id}", ctx ->
                ctx.json(new WardService().getWardByWardId(ctx.pathParam("id"))));


        this.server.exception((RuntimeException.class), (e,ctx) -> {
            e.printStackTrace();
            ctx.status(500);
            ctx.result(e.getMessage());
        });


    }

    public Javalin start(){
        return this.server.start(7031);
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
