package com.systems.programming.assembler;

import com.systems.programming.assembler.Exceptions.UnknownRegisterException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mohamed Mahmoud on 4/12/2017.
 */
public class SavedRegisters {

    private static SavedRegisters ourInstance = new SavedRegisters();

    private enum REGISTERS{A,X,L,B,S,T};

    private SavedRegisters() {

    }

    public static SavedRegisters getInstance() {
        return ourInstance;
    }

    public boolean containsValue( String reg) {
        return Arrays.stream(REGISTERS.values()).map(REGISTERS::name).anyMatch(s->s.equalsIgnoreCase(reg));
    }
    public int getValue(String reg)throws UnknownRegisterException
    {
        if(!containsValue(reg))throw new UnknownRegisterException();
        return REGISTERS.valueOf(reg).ordinal();
    }
}
