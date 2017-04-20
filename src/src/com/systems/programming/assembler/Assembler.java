package com.systems.programming.assembler;

import java.io.File;

/**
 * Created by ADMIN on 4/20/2017.
 */
public class Assembler {
    private static int locationCounter;
    private static boolean isOptableInitiallized = false;
    private static int programLength;
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
        locationCounter = 0;
    }
}
