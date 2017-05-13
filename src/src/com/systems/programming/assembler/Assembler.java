package com.systems.programming.assembler;

import java.io.File;

/**
 * Created by ADMIN on 4/20/2017.
 */
public class Assembler {
    private static int locationCounter;
    private static boolean isOptableInitiallized = false;
    private static int programLength;
    private static String inputPath = "./resources/code1.txt";
    private static String intermediatePath = "Intermediate.txt";
    private static String HTMEPath = "HTME Record.txt";
    private static String SymTablePath = "SymTable.txt";
    private static String progName;


    public static void main(String[] args) {
        init();
        Pass1.execute();
        Pass2.execute();
    }

    public static int getLocationCounter() {
        return locationCounter;
    }

    public static void setLocationCounter(int locationCounter) {
        Assembler.locationCounter = locationCounter;
    }

    public static void IncrementLocationCounterBy(int delta)
    {
        locationCounter+=delta;
    }

    public static int getProgramLength() {
        return programLength;
    }
    public static void setProgramLength(int programLength) {
        Assembler.programLength = programLength;
    }


    public static void init()
    {
        if(!isOptableInitiallized)
        {
            OpTable.getInstance().init(new File("./resources/InstructionSet.txt"));
            isOptableInitiallized = true;
        }
        //System.out.println("-----------> " + Integer.parseInt(DirectiveResolver.getStartAddress(),16));
        locationCounter = 0;
    }

    public static String getInputPath() {
        return inputPath;
    }

    public static void setInputPath(String inputPath) {
        Assembler.inputPath = inputPath;
    }

    public static String getIntermediatePath() {
        return intermediatePath;
    }

    public static void setIntermediatePath(String intermediatePath) {
        Assembler.intermediatePath = intermediatePath;
    }

    public static String getHTMEPath() {
        return HTMEPath;
    }

    public static void setHTMEPath(String HTMEPath) {
        Assembler.HTMEPath = HTMEPath;
    }

    public static String getSymTablePath() {
        return SymTablePath;
    }

    public static void setSymTablePath(String symTablePath) {
        SymTablePath = symTablePath;
    }

    public static String getProgName() {
        return progName;
    }

    public static void setProgName(String progName) {
        Assembler.progName = progName;
    }
}
