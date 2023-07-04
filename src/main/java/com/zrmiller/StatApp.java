package com.zrmiller;

import com.zrmiller.core.parser.PlaceParser2022;

import java.io.IOException;

public class StatApp {

    public static void main(String[] args){
        PlaceParser2022 parser = new PlaceParser2022();
        parser.openStream();
        int i = 0;
        System.out.println("Running...");
        try{
            while(parser.ready()){
                parser.readNextLine();
                i++;
            }
        } catch(IOException e){

        }
        System.out.println("Tile count:" + i);

    }

}
