package com.systems.programming.assembler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

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
    private static Map<String, Integer> extDefs = new TreeMap<>();
    private static List<MRecord> mRecords = new ArrayList<>();
    private static List<String> extRefs = new ArrayList<>();
    private static int skipper = -1;
    private static int currStart = 0;
    private static int nextStart = 0;
    private static boolean lastSect;
    private static String nextProgName;
    private static int proglength;

    public static void main(String[] args) {
        start();
    }

    public static int getSkipper() {
        return skipper;
    }

    public static void setSkipper(int skipper) {
        Assembler.skipper = skipper;
    }

    public static int getProglength() {
        return proglength;
    }

    public static void setProglength(int proglength) {
        Assembler.proglength = proglength;
    }

    public static void start() {
        System.out.println("<<<<<<<<<<Starting The Assembler for a new Program>>>>>>>>>>>");
        init();
        SymTab.getInstance().clear();

        extRefs.clear();
        extDefs.clear();
        mRecords.clear();
        Pass1.execute();
        System.out.println("Exited to execute pass 2 !!!!!!!!!!");
        Pass2.execute();
    }

    public static void addExtDef(String def) {
        extDefs.put(def, 0);
    }

    public static void setExtDefAddressIfExists(String def, int address) {
        if (extDefs.containsKey(def))
            extDefs.put(def, address);
    }

    public static String getExtDef() {
        return extDefs.entrySet().stream()
                .map(pair -> pair.getKey() + "_" + String.format("%06X", pair.getValue())).collect(Collectors.joining("_"));
    }

    public static int getLocationCounter() {
        return locationCounter;
    }

    public static void setLocationCounter(int locationCounter) {
        Assembler.locationCounter = locationCounter;
    }

    public static void IncrementLocationCounterBy(int delta) {
        locationCounter += delta;
    }

    public static int getProgramLength() {
        return programLength;
    }

    public static void setProgramLength(int programLength) {
        Assembler.programLength = programLength;
    }


    public static void init() {
        if (!isOptableInitiallized) {
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

    public static void appendMRecords(List<MRecord> mRecords) {
        Assembler.mRecords.addAll(mRecords);
    }

    public static void addMRecord(MRecord mRecord) {
        mRecords.add(mRecord);
    }

    public static List<MRecord> getmRecords() {
        return mRecords;
    }

    public static void addExtRef(String ref) {
        extRefs.add(ref);
    }

    public static List<String> getExtRefs() {
        return extRefs;
    }


    public static int getCurrStart() {
        return currStart;
    }

    public static void setCurrStart(int currStart) {
        Assembler.currStart = currStart;
    }

    public static int getNextStart() {
        return nextStart;
    }

    public static void setNextStart(int nextStart) {
        Assembler.nextStart = nextStart;
    }

    public static boolean isLastSect() {
        return lastSect;
    }

    public static void setLastSect(boolean lastSect) {
        Assembler.lastSect = lastSect;
    }

    public static String getNextProgName() {
        return nextProgName;
    }

    public static void setNextProgName(String nextProgName) {
        Assembler.nextProgName = nextProgName;
    }
}

