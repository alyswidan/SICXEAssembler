package com.systems.programming.assembler;

import com.systems.programming.assembler.Exceptions.AssemblerException;

import java.io.*;

/**
 * Created by ADMIN on 3/31/2017.
 */
public class Pass1 {

    private static int counter = 0;

    public static void execute() {
        System.out.println("=================pass1================");
        try (BufferedReader sourceReader = new BufferedReader(new FileReader(Assembler.getInputPath()));
             PrintWriter intermediateFileWrite = new PrintWriter(new FileWriter(Assembler.getIntermediatePath()),true);
             PrintWriter symTableWrite = new PrintWriter(new FileWriter(Assembler.getSymTablePath()),true)) {

            String line;

            while (Assembler.getSkipper() != -1) {
                sourceReader.readLine();
                Assembler.setSkipper(Assembler.getSkipper() - 1);
            }

            Assembler.setSkipper(-1);

            while ((line = sourceReader.readLine()) != null) {
                try {
                    Line parsedLine = LineParser.getInstance().parse(line);

                    if (parsedLine.isAssemblerExecutable())
                        parsedLine.execute();

                    if (parsedLine.getLabel() != null && !parsedLine.isEqu()) {
                        SymTab.getInstance().putFull(parsedLine.getLabel(), Assembler.getLocationCounter(), SymTab.Type.RELATIVE, Assembler.getProgName());
                    }

                    //System.out.println(">>>>>>>>>>>>>>>>>>>>..... " + parsedLine.getMnemonic());
                    //System.out.println("heeeeeeeeeeeeeeeeeeeeeyyyyyyyyyyyyyyyy " + parsedLine.isCSECT());

                    if (parsedLine.isComment()) intermediateFileWrite.println(parsedLine.getComment());

                    else if (parsedLine.isCSECT()) {
                        System.out.println("PASS 1 exited <><><><><><><><><><><><><><><><><><><>");
                        intermediateFileWrite.append("<><><><><><><><><><><><><><><><><><><>");
                        intermediateFileWrite.append("Control Section:" + parsedLine.getLabel());
                        //write address in first column
                        intermediateFileWrite.printf("%04X", Assembler.getLocationCounter());
                        //write the parsedLine
                        intermediateFileWrite.println(parsedLine);
                        Assembler.IncrementLocationCounterBy(parsedLine.getLength());
                        //setting the skipper value with the wanted number of skipped line
                        Assembler.setSkipper(counter + 1);
                        //Appending the SYMTAB File with the previous SYMTAB before the CSECT
                        symTableWrite.append(SymTab.getInstance().toString());
                        return;
                    }  else if (!parsedLine.isEmpty() && !parsedLine.isEnd()) {
                        //write address in first column
                        intermediateFileWrite.printf("%04X", Assembler.getLocationCounter());
                        //write the parsedLine
                        intermediateFileWrite.println(parsedLine);
                        Assembler.IncrementLocationCounterBy(parsedLine.getLength());
                    }  else if(parsedLine.isEmpty()) {
                        //intermediateFileWrite.println(line.trim());
                    }  else if(parsedLine.isEnd()){
                        //write address in first column
                        intermediateFileWrite.printf("%04X", Assembler.getLocationCounter());
                        //write the parsedLine
                        intermediateFileWrite.println(parsedLine);
                        Assembler.IncrementLocationCounterBy(parsedLine.getLength());
                        symTableWrite.append(SymTab.getInstance().toString());
                    }

                    //adding one to the counter for a line to skip
                    counter++;

                } catch (AssemblerException e) {
                    intermediateFileWrite.println(line.trim());
                    e.printStackTrace();
                    intermediateFileWrite.println("**Error^^:" + e.getClass().getSimpleName());
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
