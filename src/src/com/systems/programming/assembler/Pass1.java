package com.systems.programming.assembler;

import com.systems.programming.assembler.Exceptions.AssemblerException;

import java.io.*;
import java.util.Arrays;

/**
 * Created by ADMIN on 3/31/2017.
 */
public class Pass1 {



    public static void execute() {

        Assembler.init();
        try (BufferedReader sourceReader = new BufferedReader(new FileReader(Assembler.getInputPath()));
             PrintWriter intermediateFileWrite = new PrintWriter(new FileWriter(Assembler.getIntermediatePath()));
             PrintWriter symTableWrite = new PrintWriter(new FileWriter(Assembler.getSymTablePath()))) {

            String line;

            while ((line = sourceReader.readLine()) != null) {
                try {
                    Line parsedLine = LineParser.getInstance().parse(line);

                   if(parsedLine.isAssemblerExecutable())
                       parsedLine.execute();

                    if (parsedLine.getLabel() != null) {
                        SymTab.getInstance().put(parsedLine.getLabel(), Assembler.getLocationCounter());
                    }

                    if (parsedLine.isComment()) intermediateFileWrite.println(parsedLine.getComment());

                    else if (!parsedLine.isEmpty()) {
                        //write address in first column
                        intermediateFileWrite.printf("%04X", Assembler.getLocationCounter());
                        //write the parsedLine
                        intermediateFileWrite.println(parsedLine);
                        Assembler.IncrementLocationCounterBy(parsedLine.getLength());
                    }


                } catch (AssemblerException e) {
                    intermediateFileWrite.println(line.trim());
                    intermediateFileWrite.println("**Error^^:" + e.getClass().getSimpleName());
                }

            }

            //write the symbolTable
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
