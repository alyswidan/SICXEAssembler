package com.systems.programming.assembler;

import com.systems.programming.assembler.Exceptions.AssemblerException;

import java.io.*;

/**
 * Created by ADMIN on 3/31/2017.
 */
public class Pass1 {

    private static int counter = 0;

    public static void execute() {

        Assembler.init();
        try (BufferedReader sourceReader = new BufferedReader(new FileReader(Assembler.getInputPath()));
             PrintWriter intermediateFileWrite = new PrintWriter(new FileWriter(Assembler.getIntermediatePath()));
             PrintWriter symTableWrite = new PrintWriter(new FileWriter(Assembler.getSymTablePath()))) {

            String line;

            while ((line = sourceReader.readLine()) != null) {
                try {
                    Line parsedLine = LineParser.getInstance().parse(line);

                    if (Assembler.getSkipper() != 0) {
                        Assembler.setSkipper(Assembler.getSkipper() - 1);
                        continue;
                    }

                    if (parsedLine.isAssemblerExecutable())
                        parsedLine.execute();

                    if (parsedLine.getLabel() != null && !parsedLine.isEqu()) {
                        SymTab.getInstance().putFull(parsedLine.getLabel(), Assembler.getLocationCounter(), SymTab.Type.RELATIVE, Assembler.getProgName());
                    }


                    System.out.println(">>>>>>>>>>>>>>>>>>>>..... " + parsedLine.getMnemonic());


                    if (parsedLine.isComment()) intermediateFileWrite.println(parsedLine.getComment());

                    else if (!parsedLine.isEmpty()) {
                        //write address in first column
                        intermediateFileWrite.printf("%04X", Assembler.getLocationCounter());
                        //write the parsedLine
                        intermediateFileWrite.println(parsedLine);
                        Assembler.IncrementLocationCounterBy(parsedLine.getLength());
                    }

                    /*else if (parsedLine.getMnemonic().equalsIgnoreCase("csect")){
                        Assembler.setSkipper(counter + 1);
                        //symTableWrite.println(SymTab.getInstance());
                        Assembler.init();
                        Assembler.setLocationCounter(Integer.parseInt(DirectiveResolver.getInstance().getStartAddress()));
                        Pass1.execute();
                        pass2.execute();
                        continue;
                    }*/

                    counter++;

                } catch (AssemblerException e) {
                    intermediateFileWrite.println(line.trim());
                    e.printStackTrace();
                    intermediateFileWrite.println("**Error^^:" + e.getClass().getSimpleName());
                }

            }

            symTableWrite.println(SymTab.getInstance());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
