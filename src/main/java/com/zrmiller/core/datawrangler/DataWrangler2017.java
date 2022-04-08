package com.zrmiller.core.datawrangler;

import com.zrmiller.core.ColorConverter;
import com.zrmiller.core.TileEdit;
import com.zrmiller.core.utility.PlaceInfo;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class DataWrangler2017 extends DataWrangler {

    //    private final String directory;
    private final String fileName;

    private String downloadURL = "https://storage.googleapis.com/place_data_share/place_tiles.csv";
    private TileEdit[] tileEdits;

    public DataWrangler2017(String directory, String fileName) {
        this.directory = directory;
        this.fileName = fileName;
    }

    public void downloadFile() {
        downloadFile(fileName, downloadURL);
    }

    public boolean sortAndMinify(String source, String dest, boolean deleteSource) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(directory + source));

            ColorConverter colorConverter = new ColorConverter();
            TileEdit[] tileEdits = new TileEdit[PlaceInfo.CLEAN_LINE_COUNT];
            int lineCount = 0;
            while (reader.ready()) {
                String line = reader.readLine();
                String[] tokens = tokenizeLine(line, 5);
                if (tokens == null) continue; // Skip Empty Lines
                if (!lineStartsWithNumber(tokens[0])) continue; // Skip lines that don't start with a timestamp
                if (tokens[2] == null || tokens[2].length() == 0) continue; // Skip corrupt lines
                TileEdit edit = new TileEdit(getTimestamp(tokens[0], PlaceInfo.INITIAL_TIME_2017),
                        Short.parseShort(tokens[4]),
                        Short.parseShort(tokens[2]),
                        Short.parseShort(tokens[3]));
                tileEdits[lineCount] = edit;
                lineCount++;
//                outputStream.write(edit.toByteArray());

            }
            Arrays.sort(tileEdits);
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(directory + dest));
            for(TileEdit tile : tileEdits){
//                outputStream.write((edit.toString() + "\n").getBytes(StandardCharsets.UTF_8));
                outputStream.write(tile.toByteArray());
            }
            outputStream.close();

            reader.close();
//            outputStream.close();
            if (deleteSource) {
                File file = new File(directory + source);
                return file.delete();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
