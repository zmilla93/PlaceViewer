package com.zrmiller.core.parser;

import java.io.*;
import java.sql.Timestamp;

public class FileConverter {

    public FileConverter(){

    }

    public void firstClean(String inputPath) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputPath));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(inputPath + "_partial")));
            while (reader.ready()) {
                String line = reader.readLine();
                String[] tokens = line.split(",");
                if (tokens.length != 5 || tokens[0].length() < 5) continue;
                String time = tokens[0].substring(0, tokens[0].length() - 4);
                Timestamp timestamp = Timestamp.valueOf(time);
                String timeString = Long.toString(timestamp.getTime());
                writer.write(timeString + "," + tokens[2] + "," + tokens[3] + "," + tokens[4] + "\n");
            }
            reader.close();
            writer.close();
            System.out.println("Clean end");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void secondClean(String inputPath) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputPath + "_sorted"));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(inputPath + "_final")));
            while (reader.ready()) {
                String line = reader.readLine();
                String[] tokens = line.split(",");
                writer.write(tokens[1] + "," + tokens[2] + "," + tokens[3] + "\n");
            }
            reader.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
