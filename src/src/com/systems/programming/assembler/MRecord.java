package com.systems.programming.assembler;

import java.util.stream.Collectors;

/**
 * Created by alyswidan on 15/05/17.
 */
public class MRecord {
    int address;
    char sign;
    int length;
    String relativeTo;

    public MRecord(int address, char sign, int length, String relativeTo) {
        this.address = address;
        this.sign = sign;
        this.length = length;
        this.relativeTo = relativeTo;
    }
    public MRecord()
    {

        this.address = Assembler.getLocationCounter();
        this.sign = '+';
        this.length = 5;
        this.relativeTo = Assembler.getProgName();
    }

    public MRecord(char sign, int length, String relativeTo) {
        this.address = 0;
        this.sign = sign;
        this.length = length;
        this.relativeTo = relativeTo;
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public char getSign() {
        return sign;
    }

    public void setSign(char sign) {
        this.sign = sign;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getRelativeTo() {
        return relativeTo;
    }

    public void setRelativeTo(String relativeTo) {
        this.relativeTo = relativeTo;
    }

    @Override
    public String toString() {
        return String.format("M_%06X_%02d_%c_%s",address,length,sign,relativeTo);
    }
}
