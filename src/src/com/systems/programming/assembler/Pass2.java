package com.systems.programming.assembler;

import com.systems.programming.assembler.Exceptions.AssemblerException;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Mohamed Mahmoud on 4/11/2017.
 */
public class Pass2 {


    public static void execute() {
        Assembler.init();

        try (BufferedReader sourceReader = new BufferedReader(new FileReader("Intermediate.txt"));
             PrintWriter HTMEWriter = new PrintWriter(new FileWriter("HTME Record.txt"))) {

            LineParser.getInstance().setMode(LineParser.Mode.DEEP);
            String line;

            List<Line> currentTRecord = new ArrayList<>();
            while ((line = sourceReader.readLine()) != null) {
                line = Arrays.stream(line.split("\\s+")).skip(1).collect(Collectors.joining("  "));
                System.out.println(line);
                Line parsedLine = LineParser.getInstance().parse(line);
                parsedLine.setAddress();
                if (parsedLine.isAssemblerExecutable()) {//is this an executable
                    parsedLine.execute();//execute it

                    if (parsedLine.isStart())//is it a start
                        HTMEWriter.println(createHRecord(parsedLine.getLabel(), parsedLine.getOperand()));

                    if (parsedLine.isEnd())//is it an end
                        HTMEWriter.println(createERecord(parsedLine.getOperand()));
                } else {
                    if (parsedLine.getObjectCode().getOpcode() != null)/* if this is not a reserve*/ {

                        if (currentTRecord.size() + parsedLine.getObjectCode().getLength() <= 30)//if it fits in record
                            currentTRecord.add(parsedLine);
                        else//if it doesn't fit
                        {
                            HTMEWriter.println(createTRecord(currentTRecord));//print current record
                            currentTRecord.add(parsedLine);
                        }
                    } else {// if it is a reserve
                        HTMEWriter.println(createTRecord(currentTRecord));
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AssemblerException e) {
            e.printStackTrace();
        }
    }

    private static String createTRecord(List<Line> currentTRecord) {
        if (currentTRecord.size() == 0)
            return "";
        String builder = "T" +
                currentTRecord.get(0).getAddress() +//add the address
                String.format("%02x",
                                currentTRecord.stream()//get the stream
                                .mapToInt(line -> line.getObjectCode().getLength())//map it to length of object code
                                        .sum()) //sum all that up
                +
                        currentTRecord.stream()
                        .map(line -> line.getObjectCode().toString())//map to object code strings
                        .collect(Collectors.joining(""));//join those into one string
        currentTRecord.clear();//clear the list
        return builder;
    }

    private static String createHRecord(String label, String operand) {
        int address = Integer.parseInt(operand, 16);
        return "H" +
                String.format("%6s", label) +
                String.format("%06x", address) +
                String.format("%06x", Assembler.getProgramLength());
    }

    private static String createERecord(String operand) {
        int address = Integer.parseInt(operand, 16);
        return "E" +
                String.format("%06x", address);
    }
}
