package co.wethinkcode.healthsafe;

import io.javalin.Javalin;

import java.util.ArrayList;


public class IngestionServiceApp {

    private final Javalin server;
    ArrayList<WardRecordModel> cleanedRecords = CsvCleaner.getCleanedWards();

    public IngestionServiceApp() throws Exception {
    this.server = Javalin.create();
    this.server.get("/health", ctx -> ctx.result("OK"));
    this.server.get("/wards",ctx -> ctx.json(cleanedRecords));

    }

    public Javalin start(){
        return this.server.start(7030);
    }

    public Javalin stop(){
        return this.server.stop();
    }

    public static void main(String[] args) throws Exception {
        IngestionServiceApp api = new IngestionServiceApp();
        api.start();

//        Javalin app = Javalin.create().start(7030);
//        String filePath = "wards-outdated.csv";
//        ArrayList<WardRecordModel> cleaned = CsvCleaner.getCleanedWards();
//
//        app.get("/health", ctx -> ctx.result("OK"));
//            app.get("/wards", ctx -> {
//                ctx.json(cleaned);
//        });

        // TODO: read and clean src/main/resources/wards-outdated.csv (wards, wings, specialist departments data —
        // trim whitespace, fix casing, normalize dates/booleans) and expose the
        // cleaned records here for the other services to consume.




    }
}


