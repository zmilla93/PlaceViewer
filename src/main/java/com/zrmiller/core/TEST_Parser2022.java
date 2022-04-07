package com.zrmiller.core;

import java.io.*;
import java.nio.ByteBuffer;

public class TEST_Parser2022 {

    private String directory = "D:/Place/";
    private String fileNameBinary = "Place_1_Micro.placetiles";
    private String fileName = "Place_1_Mini.txt";

    public TEST_Parser2022() {

    }

    public void readAllBinary() {
        try {
            BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(directory + fileNameBinary));
//            FileInputStream inputStream = new FileInputStream(directory + fileNameBinary);
            byte[] line = new byte[10];
            int size = 1;
            int i = 0;
            System.out.println("Reading...");
            while (size > -1) {
                size = inputStream.read(line);
                ByteBuffer buffer = ByteBuffer.wrap(line);
                int timestamp = buffer.getInt();
                short color = buffer.getShort();
                short x = buffer.getShort();
                short y = buffer.getShort();
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readAll() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(directory + fileName));
            while (reader.ready()) {
                String line = reader.readLine();
                String[] tokens = TEMP_tokenizeLine(line, 5);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String[] TEMP_tokenizeLine(String input, int tokenCount) {
        String[] tokens = new String[tokenCount];
        StringBuilder builder = new StringBuilder();
        int tokenIndex = 0;
        for (int i = 0; i < input.length(); i++) {
            if (tokenIndex >= tokenCount) {
                System.out.println("BADNESS:" + input);
                return null;
            }
            if (input.charAt(i) == ',') {
                tokens[tokenIndex] = builder.toString();
                builder.setLength(0);
                tokenIndex++;
            } else {
                builder.append(input.charAt(i));
            }
        }
        tokens[tokenIndex] = builder.toString();
        return tokens;
    }


}
