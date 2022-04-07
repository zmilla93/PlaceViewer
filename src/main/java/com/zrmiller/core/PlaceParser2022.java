package com.zrmiller.core;

import java.io.*;
import java.nio.ByteBuffer;

public class PlaceParser2022 {

    private String directory = "D:/Place/";
    private String fileName = "Place_1_Micro.placetiles";

    public PlaceParser2022() {

    }

    public void readAll() {
        try {
            FileInputStream inputStream = new FileInputStream(directory + fileName);
            byte[] line = new byte[10];
            int size = 1;
            System.out.println("Reading...");
            while (size > -1) {
                size = inputStream.read(line);
                ByteBuffer buffer = ByteBuffer.wrap(line);
                int timestamp = buffer.getInt();
                short color = buffer.getShort();
                short x = buffer.getShort();
                short y = buffer.getShort();
                System.out.println("X : " + x);
                System.out.println("y : " + y);
                break;
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


//        try {
//
//            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(directory + fileName));
////            TEMP_TileEdit edit = (TEMP_TileEdit) inputStream.readObject();
////            System.out.println(edit.x);
////            System.out.println(edit.y);
////            byte[] line = new byte[4];
////            int i = inputStream.read(line);
////            System.out.println("I:" + i);
//////            int p1 = line[0];
////            short x = line[0];
////            short y = line[2];
//////            System.out.println("p1:"+ p1);
////            System.out.println("x:"+ x);
////            System.out.println("y:"+ y);
////            inputStream.close();
//
////            BufferedReader reader = new BufferedReader(new FileReader(directory + fileName));
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
    }


}
