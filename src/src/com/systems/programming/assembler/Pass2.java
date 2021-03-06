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

    private static Integer counter = 0;

    public static void execute() {
        System.out.println("===============pass2================");

        try (LineNumberReader sourceReader = new LineNumberReader(new FileReader(Assembler.getIntermediatePath()));
             PrintWriter HTMEWriter = new PrintWriter(new FileWriter(Assembler.getHTMEPath(),true),true)) {
            int currLine = 1;
            LineParser.getInstance().setMode(LineParser.Mode.DEEP);
            String line;
            List<Line> currentTRecord = new ArrayList<>();
            //System.out.println("<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>>>> ");
            for (int i = 0; i < Assembler.getCurrStart(); i++) {
                sourceReader.readLine();
            }

            while ((line = sourceReader.readLine()) != null) {
               // System.out.println("line = " + line);
                String address = line.split("\\s+")[0];
                line = Arrays.stream(line.split("\\s+")).skip(1).collect(Collectors.joining("  "));
                int xx = Assembler.getLocationCounter();
                try {
                    xx = Integer.parseInt(address,16);
                }
                catch (NumberFormatException ex){}

                Assembler.setLocationCounter(xx);
                Line parsedLine = new Line("");
                try
                {
                     parsedLine = LineParser.getInstance().parse(line);
                }
                catch (AssemblerException ex)
                {
                    String s = String.format("***Error:%s at line %d address %06X \n",ex.getClass().getSimpleName(),currLine,xx);
                    HTMEWriter.append(s);
                    //HTMEWriter.printf("***Error:%s at line %d address %06X \n",ex.getClass().getSimpleName(),currLine,xx);
                    ex.printStackTrace();
                }


                if (!parsedLine.isComment() && !parsedLine.isEmpty()){
                    parsedLine.setAddress(Integer.parseInt(address, 16));
                }
                else continue;

                if (parsedLine.isAssemblerExecutable()) {//is this an executable
                    parsedLine.execute();//execute it
                    if (parsedLine.isStart())//is it a start
                        HTMEWriter.print(createHRecord(parsedLine.getLabel(), parsedLine.getOperand()));

                   // System.out.println("<<<<<<<<<<<<<==============>>>>>>>>>. " + parsedLine.isCSECT());

                    if(parsedLine.isCSECT()){

                        if(currentTRecord.size()>0) {
                            HTMEWriter.append(createTRecord(currentTRecord, counter));
                        }

                        HTMEWriter.append(createMRecords(Assembler.getmRecords()));
                    }
                    if (parsedLine.isEnd())//is it an end
                    {
                        if(currentTRecord.size()>0) {
                            HTMEWriter.append(createTRecord(currentTRecord, counter));
                        }

                        HTMEWriter.append(createMRecords(Assembler.getmRecords()));

                        HTMEWriter.append(createERecord(parsedLine.getOperand()));
                    }

                } else if (parsedLine.getObjectCode() != null && !parsedLine.getObjectCode().toString().equals("null"))/* if this is not a reserve*/ {
                    int len = parsedLine.getObjectCode().toString().length()/2;
                    if (parsedLine.getObjectCode().isFormat4() && parsedLine.getArgType() != Line.ArgType.IMMEDIATE) {
                        Assembler.addMRecord(new MRecord(parsedLine.getOperand()));
                    }
                    if (counter + len <= 30)//if it fits in record
                    {
                        counter+= len;
                        currentTRecord.add(parsedLine);

                    } else//if it doesn't fit
                    {
                        HTMEWriter.append(createTRecord(currentTRecord,counter));//print current record
                        counter = len;
                        currentTRecord.add(parsedLine);

                    }
                }else if(parsedLine.getMnemonic().equalsIgnoreCase("extref")) {
                    HTMEWriter.append(createRRecords());
                }
                else if(parsedLine.getMnemonic().equalsIgnoreCase("extdef")){
                    HTMEWriter.append(createDRecords());
                }else {// if it is a reserve
                    HTMEWriter.append(createTRecord(currentTRecord,counter));
                    counter = 0;
                }
                HTMEWriter.flush();
                currLine++;
            }

            //System.out.println(Assembler.getExtDef());

            if(!Assembler.isLastSect()) {
                HTMEWriter.println("============================================");
                HTMEWriter.println(createHRecord(Assembler.getNextProgName(), "0"));
                Assembler.setCurrStart(Assembler.getNextStart());
                Assembler.setProgName(Assembler.getNextProgName());
                Assembler.start();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AssemblerException e) {
            e.printStackTrace();
        }
    }

    private static String createRRecords() {
        return "R_"+Assembler.getExtRefs().stream().map(ref->String.format("%-6s",ref))
                                    .collect(Collectors.joining("_"))+"\n";
    }
    private static String createDRecords() {
        return "D_"+Assembler.getExtDef()+"\n";
    }

    private static String createMRecords(List<MRecord> mRecords) {

        return mRecords.stream().map(MRecord::toString).collect(Collectors.joining("\n"))+"\n";
    }

    private static String createTRecord(List<Line> currentTRecord,int len) {
        if (currentTRecord.size() == 0)
            return "";
        String record = "T"+
                "_" +
                String.format("%06X",currentTRecord.get(0).getAddress()) +//add the address
                "_" +
                String.format("%02X",
                        len) //sum all that up
                +
                "_" +
                currentTRecord.stream()
                        .map(line -> line.getObjectCode().toString())//map to object code strings
                        .collect(Collectors.joining("_"));//join those into one string
        currentTRecord.clear();//clear the list


        return record + "\n";
    }

    private static String createHRecord(String label, String operand) {
        int address = Integer.parseInt(operand, 16);
        return "H" +
                "_" +
                String.format("%-6s", label) +
                "_" +
                String.format("%06X", address) +
                "_" +
                String.format("%06X", Assembler.getProglength())
                + "\n";
    }

    private static String createERecord(String operand) {
        int address = Integer.parseInt(operand);
        return "E" +
                "_" +
                String.format("%06X", address);
    }
}