package com.zrmiller.core.utility;

import java.io.*;
import java.util.HashSet;

/**
 * Random utility code that was used once for modifying data.
 */
public class OneTimeUtil {

    private static String directory = "D:/Place";
    private static String sortFileName = "";
    private static String originalFileName = "";

    public static long getDownloadSize2022() {
        return 0;
    }

    public void cleanTimestamps(String inputPath, String outputPath) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(directory + inputPath));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(directory + outputPath)));
            while (reader.ready()) {
                String[] tokens = tokenizeLine(reader.readLine(), 2);
                if (tokens == null) continue;
                writer.write(tokens[1]);
                if (reader.ready())
                    writer.write(",");
            }
            reader.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void scanForColors(String source) {
        try {
            HashSet<String> colors = new HashSet<>();
            BufferedReader reader = new BufferedReader(new FileReader(directory + source));
            while (reader.ready()) {
                String[] tokens = tokenizeLine(reader.readLine(), 5);
                if (tokens == null) continue;
                colors.add(tokens[2]);
            }
            for (String s : colors) {
                System.out.println("COLOR :: " + s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String[] tokenizeLine(String input, int tokenCount) {
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

    private String indexedName(String input, int index) {
        return input.replaceFirst("INDEX", Integer.toString(index));
    }

    public void dumpTimestamps(String outputDirectory) {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(directory + sortFileName)));
            for (int i = 0; i < 78; i++) {
                BufferedReader reader = new BufferedReader(new FileReader(directory + indexedName(originalFileName, i)));
                if (reader.ready()) {
                    reader.readLine();
                    String[] tokens = tokenizeLine(reader.readLine(), 5);
                    if (tokens == null) continue;
                    writer.write(tokens[0] + "," + i + "\n");
                }
            }
            writer.close();
        } catch (IOException e) {

        }
    }

}
