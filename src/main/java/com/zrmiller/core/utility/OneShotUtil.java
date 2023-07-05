package com.zrmiller.core.utility;

import com.zrmiller.modules.strings.FileName;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Random utility code that was used once for modifying data.
 * Mostly broken now, just here for legacy purposes.
 */
public class OneShotUtil {

    private static String directory = "D:/Place";
    private static String sortFileName = "";
    private static String originalFileName = "";

    public static String runDataScan(String directory) {
        TileEdit[] tiles = new TileEdit[PlaceInfo.FILE_COUNT_2022];
        HashMap<Integer, Integer> orderMap = new HashMap<>();
        int badTimes = 0;
        try {
            for (int i = 0; i < PlaceInfo.FILE_COUNT_2022; i++) {
                BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(directory + FileName.BINARY_2022.getIndexedName(i)));
                byte[] meta = new byte[6];
                inputStream.read(meta);
                // Read All Tiles
                byte[] tileBytes = new byte[TileEdit.BYTE_COUNT];
                int bytesRead = 0;
//                ArrayList<TileEdit> tileEdits = new ArrayList<>();
                int time = 0;
                boolean badTime = false;
                while ((bytesRead = inputStream.read(tileBytes)) > 0) {
                    TileEdit tile = new TileEdit(tileBytes);
//                    orderMap.put(tile.timestamp, i);
                    tiles[i] = tile;
                    if (tile.timestamp < time) {
                        badTime = true;
                    }
                    time = tile.timestamp;
                }
                if (badTime) badTimes++;
                inputStream.close();
                System.out.println("Read #" + i);
            }
        } catch (IOException e) {

        }
        System.out.println("BAD TIMES : " + badTimes);
        System.out.println("TILE:" + tiles);
        Arrays.sort(tiles);
        int[] order = new int[tiles.length];
//        for (int i = 0; i < order.length; i++) {
//            order[i] = orderMap.get(tiles[i].timestamp);
//        }
        System.out.println(Arrays.toString(order));
        System.out.println(order.length);
        return null;
    }

    public static int getLineCount(String directory) {
        TileEdit[] tiles = new TileEdit[PlaceInfo.FILE_COUNT_2022];
        int lineCount = 0;
        try {
            for (int i = 0; i < PlaceInfo.FILE_COUNT_2022; i++) {
                BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(directory + FileName.BINARY_2022.getIndexedName(i)));
                byte[] meta = new byte[6];
                inputStream.read(meta);
                // Read All Tiles
                byte[] tileBytes = new byte[TileEdit.BYTE_COUNT];
                int bytesRead = 0;
                int time = 0;
                while ((bytesRead = inputStream.read(tileBytes)) > 0) {
                    TileEdit tile = new TileEdit(tileBytes);
                    tiles[i] = tile;
                    lineCount++;
                }
                inputStream.close();
            }
            System.out.println("Line count:");
        } catch (IOException e) {

        }
        return lineCount;
    }

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
