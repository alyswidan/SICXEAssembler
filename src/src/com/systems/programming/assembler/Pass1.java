package com.systems.programming.assembler;

import com.systems.programming.assembler.Exceptions.AssemblerException;

import java.io.*;
import java.util.Arrays;

/**
 * Created by ADMIN on 3/31/2017.
 */
public class Pass1 {

    private static int locationCounter = 0;

    public static void main(String[] args) {

        File f = new File("./resources");
        System.out.println(Arrays.toString(f.listFiles()));
        OpTable.getInstance().init(new File("./resources/InstructionSet.txt"));
        System.out.println(OpTable.getInstance());


        try (BufferedReader sourceReader = new BufferedReader(new FileReader("./resources/code1.txt"));
             PrintWriter intermediateFileWrite = new PrintWriter(new FileWriter("Intermediate.txt"));
             PrintWriter symTableWrite = new PrintWriter(new FileWriter("SymTable.txt"))) {
            String line;
            while ((line = sourceReader.readLine()) != null) {

                try {
                    LineParser.getInstance().setMod(1);
                    Line parsedLine = LineParser.getInstance().parse(line);

                    if (parsedLine.getMnemonic() != null && parsedLine.isStart()) {
                        locationCounter = Integer.parseInt(parsedLine.getOperand(), 16);
                        //write address in first column
                        intermediateFileWrite.printf("0x%03x", locationCounter);
                        //write the parsedLine
                        intermediateFileWrite.println(parsedLine);
                        continue;
                    }


                    if (parsedLine.getLabel() != null) {
                        SymTab.getInstance().put(parsedLine.getLabel(), locationCounter);
                    }

                    if (parsedLine.isComment()) System.out.println(parsedLine.getComment());

                    else if (!parsedLine.isEmpty()) {
                        //write address in first column
                        intermediateFileWrite.printf("0x%03x", locationCounter);
                        //write the parsedLine
                        intermediateFileWrite.println(parsedLine);
                        locationCounter += parsedLine.getLength();
                    }


/*                *//*for debugging purposes *//*
                System.out.println(line);
                System.out.println("_________________________________________________");
                System.out.println("label: " + parsedLine.getLabel());
                System.out.println("mnemonic: " + parsedLine.getMnemonic());
                System.out.println("operand: " + parsedLine.getOperand());
                System.out.println("comment : " + parsedLine.getComment());
                System.out.println("=================================================");*/

                } catch (AssemblerException e) {
                    intermediateFileWrite.println(line.trim());
                    intermediateFileWrite.println("**Error^^:" + e.getClass().getSimpleName());
                }

            }
            symTableWrite.println("====================================");
            symTableWrite.printf("|%10s%5c%10s%10c\n", "Symbol", '|', "address", '|');
            symTableWrite.println("====================================");
            SymTab.getInstance().getTable().forEach((k, v) -> symTableWrite.printf("|%10s%5c%10x%10c\n", k, '|', v, '|'));
            symTableWrite.println("====================================");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
