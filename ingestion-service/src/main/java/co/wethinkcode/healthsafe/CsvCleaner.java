package co.wethinkcode.healthsafe;

import com.opencsv.CSVReader;

import java.util.ArrayList;
import java.util.ArrayList.*;
import java.util.HashSet;
import java.util.List;
import java.io.*;
import java.util.Set;

import static co.wethinkcode.healthsafe.CsvReader.*;


public class CsvCleaner {


    public static ArrayList<WardRecordModel> getCleanedWards() throws Exception {

        ArrayList<WardRecordModel> cleaned = new ArrayList<>();
        Set<String> wardIds = new HashSet<>();

        List<String[]> rows;
        try(
        InputStream input = CsvCleaner.class.getClassLoader()
                .getResourceAsStream("wards-outdated.csv")) {
            if (input == null) {
                throw new FileNotFoundException("wards-outdated.csv not found");
            }


            try (CSVReader reader = new CSVReader(new InputStreamReader(input))) {
                rows = reader.readAll();
            }
        }


//        try (Reader reader = new FileReader("ingestion-service/src/main/resources/wards-outdated.csv")) {
//            rows = new CSVReader(reader).readAll();
//        }

        for (int i = 1; i < rows.size(); i++) {
            String[] elem = rows.get(i);
            WardRecordModel cleanedElem = cleanRow(elem);

            if (wardIds.add(cleanedElem.getWardId())) {
                cleaned.add(cleanedElem);
            }
          //  cleaned.add(cleanedElem);

        }
        return cleaned;
    }



        public static void main(String[] args) throws Exception {
            CsvCleaner cleaner = new CsvCleaner();
            ArrayList<WardRecordModel> cleaned = cleaner.getCleanedWards();
            //ArrayList<WardRecordModel> cleaned = new getCleanedWards();

            cleaned.forEach(clean -> {
                System.out.println("---");
                System.out.println(clean.getWardId());
                System.out.println(clean.getWing().toString());
                System.out.println(clean.getDepartment().toString());
                System.out.println(clean.getBedsAvailable());
            });
        }

        public static WardRecordModel cleanRow(String[] row) {

            String wardId = cleanText(row[0]).toUpperCase();
            String wing = toTitleCase(cleanText(row[1]));
            String department = toTitleCase(cleanText(row[2]));

            String rawBeds = cleanText(row[3]);
            Integer beds = null;
            String notes = null;
            try {
                int value = Integer.parseInt(rawBeds);
                if (value >= 0 && value < 100) { // sanity bound, tune as you like
                    //beds = Integer.parseInt(String.valueOf(value));
                    beds = value;
                } else {
                    notes = "bedsAvailable out of realistic range ('" + rawBeds + "')";
                }
            } catch (NumberFormatException e) {
                notes = "bedsAvailable was non-numeric ('" + rawBeds + "')";
            }

//            System.out.println(wardId);
//            System.out.println(wing);
//            System.out.println(department);
//            System.out.println(beds);
//            System.out.println(notes);

            return new WardRecordModel(wardId, wing, department, beds, notes);
        }



        // trims outer whitespaceand double spaces
        private static String cleanText(String value) {
            if (value == null || value.trim().isEmpty()) return "not available";
            return value.trim().replaceAll("\\s+", " ");
        }

        private static String toTitleCase(String value) {
            if (value.isEmpty()) return value;
            String[] words = value.toLowerCase().split(" ");
            StringBuilder sb = new StringBuilder();
            for (String w : words) {
                if (!w.isEmpty()) {
                    sb.append(Character.toUpperCase(w.charAt(0))).append(w.substring(1)).append(" ");
                }
            }
            return sb.toString().trim();
        }
    }
