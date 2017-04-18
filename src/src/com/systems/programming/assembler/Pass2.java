package com.systems.programming.assembler;

import com.systems.programming.assembler.Exceptions.AssemblerException;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Mohamed Mahmoud on 4/11/2017.
 */
public class Pass2 {

    static List<String> codeList = new ArrayList<>();
    private static int byteCounter = 0;
    private static int locationCounter = 0;

    public static void main(String[] args) {

        File f = new File("./resources");
        System.out.println(Arrays.toString(f.listFiles()));
        SavedRegisters.getInstance().init(new File("./resources/SavedRegisters.txt"));
        //System.out.println(OpTable.getInstance());

        try (BufferedReader sourceReader = new BufferedReader(new FileReader("./resources/code1.txt"));
             PrintWriter HTMEFileWrite = new PrintWriter(new FileWriter("HTME Record.txt"));) {
            String line;

            HTMEFileWrite.printf("H_PROGRM_%s_%s", Line.getStart(), Line.getpLength());
            while ((line = sourceReader.readLine()) != null) {

                try {
                    Line parsedLine = LineParser.getInstance().parse(line);

                    if (!parsedLine.isEmpty()) {
                        //write the parsedLine
                        codeList.add(parsedLine.getObjcode());
                        locationCounter += parsedLine.getLength();
                    }
                    System.out.println(Line.getStart());

                    for (int i=0;i<codeList.size();i++){
                        if(byteCounter == 0){

                        }
                    }

                } catch (AssemblerException e) {
                    /*HTMEFileWrite.println(line.trim());
                    HTMEFileWrite.println("**Error^^:" + e.getClass().getSimpleName());*/
                }

            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getLocationCounter() {
        return locationCounter;
    }
}
